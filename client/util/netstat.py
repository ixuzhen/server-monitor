import errno
import os
from collections import namedtuple, defaultdict
from enum import Enum
from util.process_util import pids, readlink

TCP = namedtuple('TCP', ['localAddress', 'localPort', 'remoteAddress', 'remotePort',
                        'state','inode', 'pid','pname','type'],defaults=['', '', '', '','','', -1,'','tcp'])
UDP = namedtuple('UDP', ['localAddress', 'localPort', 'remoteAddress', 'remotePort',
                        'state','inode', 'pid','pname','type'],defaults=['', '', '', '','','', -1,'','udp'])


class ConnectionState(Enum):
    TCP_ESTABLISHED=1
    TCP_SYN_SENT=2
    TCP_SYN_RECV=3
    TCP_FIN_WAIT1=4
    TCP_FIN_WAIT2=5
    TCP_TIME_WAIT=6
    TCP_CLOSE=7
    TCP_CLOSE_WAIT=8
    TCP_LAST_ACL=9
    TCP_LISTEN=10
    TCP_CLOSING=11


def parsingIpv4Address(address):
    res = ''
    for i in range(3,-1,-1):
        res += str(int(address[i*2: i*2+2], 16))
        if i!=0:
            res+='.'
    return res

def parsingPort(port):
    return int(port, 16)

# 根据状态来选出TCP信息，比如选出正在监听的TCPsocket：
# get_TCP_by_state(ConnectionState.TCP_LISTEN.value)
# 不包括本机去掉127.0.0.*
# file_name 可能是 ‘tcp’ 也可能是 ‘tcp6’
def get_TCP_by_state(file_name, state_want):
    with open('/proc/net/%s'%(file_name), "rt") as f:
        lines = f.readlines()
    lines.pop(0)
    res = []
    for line in lines:
        fields = line.split()
        state = int(fields[3], 16)
        if state!=state_want:
            continue
        if file_name == 'tcp':
            localAddress = parsingIpv4Address(fields[1].split(':')[0])
            if localAddress.startswith("127.0.0"):
                continue
            remoteAddress = parsingIpv4Address(fields[2].split(':')[0])
        localPort = parsingPort(fields[1].split(':')[1])
        remotePort = parsingPort(fields[2].split(':')[1])
        inode = int(fields[9])
        # state_str = ConnectionState(state)
        # print(f'{localAddress}:{localPort}\t{remoteAddress}:{remotePort}  {state_str}\t{inode}')
        res.append(TCP(localAddress,localPort,remoteAddress,remotePort, state, inode, type=file_name))
    return res


def get_UDP():
    with open('/proc/net/udp', "rt") as f:
        lines = f.readlines()
    lines.pop(0)
    res = []
    for line in lines:
        fields = line.split()
        # 解析第一个字段
        localAddress = parsingIpv4Address(fields[1].split(':')[0])
        if localAddress.startswith("127.0.0"):
            continue
        localPort = parsingPort(fields[1].split(':')[1])
        remoteAddress = parsingIpv4Address(fields[2].split(':')[0])
        remotePort = parsingPort(fields[2].split(':')[1])
        inode = int(fields[9])
        state = int(fields[3], 16)
        # state_str = ConnectionState(state)
        # print(f'{localAddress}:{localPort}\t{remoteAddress}:{remotePort}  {state_str}\t{inode}')
        res.append(UDP(localAddress,localPort,remoteAddress,remotePort, state, inode))
    return res

# 只会查找出正在监听的和已经建立连接的socket
def get_TCP_UDP_with_pid():
    tcps_established = get_TCP_by_state('tcp',ConnectionState.TCP_ESTABLISHED.value)
    tcps_listen = get_TCP_by_state('tcp',ConnectionState.TCP_LISTEN.value)
    udps = get_UDP()
    map = dict()
    for tcp in tcps_established:
        map[tcp.inode] = tcp
    for tcp in tcps_listen:
        map[tcp.inode] = tcp
    for udp in udps:
        map[udp.inode] = udp

    all_pid = pids()
    for pid in all_pid:
        try:
            for fd in os.listdir("/proc/%s/fd" % (pid)):
                try:
                    inode = readlink("/proc/%s/fd/%s" % (pid, fd))
                except (FileNotFoundError, ProcessLookupError):
                    # 有可能查询不到该文件
                    continue
                except OSError as err:
                    if err.errno == errno.EINVAL:
                        # 不是一个链接
                        continue
                    raise
                else:
                    if inode.startswith('socket:['):
                        inode = int(inode[8:][:-1])
                        if inode in map:
                            with open("/proc/%s/stat" % (pid), 'r') as f:
                                pname = f.read().split(' ')[1]
                            map[inode] = map[inode]._replace(pid=pid,pname=pname[1:-1])
        except PermissionError:
            pass


    return map.values()



def test():
    print("--------------------")
    tcps = get_TCP_by_state(ConnectionState.TCP_LISTEN.value)
    for tcp in tcps:
        print(tcp)
    print('--------------')
    udps = get_UDP()
    for udp in udps:
        print(udp)

def get_proc_inodes( pid):
    inodes = defaultdict(list)
    try:
        for fd in os.listdir("/proc/%s/fd" % ( pid)):
            try:
                inode = readlink("/proc/%s/fd/%s" % ( pid, fd))
            except (FileNotFoundError, ProcessLookupError):
                # 有可能查询不到该文件
                continue
            except OSError as err:
                if err.errno == errno.EINVAL:
                    # not a link
                    continue
                raise
            else:
                if inode.startswith('socket:['):
                    # the process is using a socket
                    inode = inode[8:][:-1]
                    inodes[inode].append((pid, int(fd)))
    except PermissionError:
        pass

    return inodes


if __name__ == '__main__':
    infos = get_TCP_UDP_with_pid()
    for info in infos:
        print(info)




