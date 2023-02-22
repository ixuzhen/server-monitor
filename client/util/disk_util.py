import os
from collections import namedtuple
# 每一个扇区的字节数
DISK_SECTOR_SIZE = 512
# 分区信息
sdiskpart = namedtuple('sdiskpart', ['device', 'mountpoint', 'fstype', 'opts'])
# 硬盘的使用情况
sdiskusage = namedtuple('sdiskusage', ['total', 'used', 'free', 'percent'])
# 分区和使用情况汇总信息
sdiskpart_usage = namedtuple('sdiskpart_usage', ['device', 'mountpoint', 'fstype','total', 'used', 'free', 'percent'])
# 硬盘的负载
disk_io_counters_type = namedtuple('disk_io_counters_type', ['name','reads', 'writes', 'rbytes', 'wbytes', 'rtime', 'wtime',
                         'reads_merged', 'writes_merged', 'busy_time'])

# 参考文章 https://docs.kernel.org/admin-guide/iostats.html
def read_diskstats():
    with open('/proc/diskstats', "rt") as f:
        lines = f.readlines()

    for line in lines:
        fields = line.split()
        flen = len(fields)
        if flen == 15:
            # Linux 2.4
            name = fields[3]
            reads = int(fields[2])
            (reads_merged, rbytes, rtime, writes, writes_merged,
                wbytes, wtime, _, busy_time, _) = map(int, fields[4:14])
        elif flen == 14 or flen >= 18:
            # Linux 2.6+, line referring to a disk
            name = fields[2]
            (reads, reads_merged, rbytes, rtime, writes, writes_merged,
                wbytes, wtime, _, busy_time, _) = map(int, fields[3:14])
        elif flen == 7:
            # Linux 2.6+, line referring to a partition
            name = fields[2]
            reads, rbytes, writes, wbytes = map(int, fields[3:])
            rtime = wtime = reads_merged = writes_merged = busy_time = 0
        else:
            raise ValueError("not sure how to interpret line %r" % line)
        yield (name, reads, writes, rbytes, wbytes, rtime, wtime,
                reads_merged, writes_merged, busy_time)


def read_sysfs():
    for block in os.listdir('/sys/block'):
        for root, _, files in os.walk(os.path.join('/sys/block', block)):
            if 'stat' not in files:
                continue
            with open(os.path.join(root, 'stat'), 'rt') as f:
                fields = f.read().strip().split()
            name = os.path.basename(root)
            (reads, reads_merged, rbytes, rtime, writes, writes_merged,
                wbytes, wtime, _, busy_time) = map(int, fields[:10])
            yield (name, reads, writes, rbytes, wbytes, rtime,
                    wtime, reads_merged, writes_merged, busy_time)

# 硬盘的读写状态
def disk_io_counters(all=False):
    res = []
    if os.path.exists('/proc/diskstats'):
        gen = read_diskstats()
    elif os.path.exists('/sys/block'):
        gen = read_sysfs()
    else:
        raise RuntimeError('找到/proc/diskstats 和 /sys/block, 无法获取磁盘信息')

    for entry in gen:
        (name, reads, writes, rbytes, wbytes, rtime, wtime, reads_merged,
            writes_merged, busy_time) = entry
        if not all:
            if name.startswith('loop'):
                continue
        # 把扇区数换成字节数
        rbytes *= DISK_SECTOR_SIZE
        wbytes *= DISK_SECTOR_SIZE
        res.append(disk_io_counters_type(name, reads, writes, rbytes, wbytes, rtime, wtime,
                         reads_merged, writes_merged, busy_time))
    return res



# 硬盘的分区情况
def disk_partition(all=False):
    fstypes = set()
    # filesystems 文件解释
    # https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/4/html/reference_guide/s2-proc-filesystems
    # 获取操作系统支持的文件系统
    with open('/proc/filesystems', 'rt') as f:
        for line in f:
            line = line.strip()
            if not line.startswith('nodev'):
                fstypes.add(line)
            else:
                fstype = line.split()[1]
                if fstype =='zfs':
                    fstypes.add('zfs')
    # 获取挂载了的分区信息
    # https://serverfault.com/questions/267609/how-to-understand-etc-mtab
    # /etc/mtab 其实是 /proc/self/mount 的 link
    if os.path.isfile('/etc/mtab'):
        mount_path = '/etc/mtab'
    else:
        mount_path = '/proc/self/mounts'
    patitions = []
    with open(mount_path, 'rt') as f:
        for line in f:
            patitions.append(line)
    retlist = []
    for patition in patitions:
        patition = patition.strip().split()
        device = patition[0]
        mountpoint = patition[1]
        fstype = patition[2]
        opts = patition[3]
        if device=='none':
            device = ''
        if not all:
            if device=='' or fstype not in fstypes or device.startswith('/dev/loop'):
                continue
        
        ntuple = sdiskpart(device, mountpoint, fstype, opts)
        retlist.append(ntuple)
    return retlist
        
def disk_usage(path):
    st = os.statvfs(path)
    total = (st.f_blocks * st.f_frsize)
    avail_to_root = (st.f_bfree * st.f_frsize)
    avail_to_user = (st.f_bavail * st.f_frsize)
    used = (total - avail_to_root)
    total_user = used + avail_to_user
    usage_percent_user = round((float(used) / total_user) * 100 ,2)
    return sdiskusage(total=total, used=used, free=avail_to_user, percent=usage_percent_user)

def get_all_disk_usage(all=False):
    patitions = disk_partition(all=False)
    res = []
    for patition in patitions:
        usage = disk_usage(patition.mountpoint)
        res.append(sdiskpart_usage(device=patition.device, mountpoint=patition.mountpoint, 
                                   fstype=patition.fstype, total=usage.total, used=usage.used, free=usage.free, percent=usage.percent))
        
    return res
       
if __name__ == '__main__':
    
    print('磁盘使用情况：')
    res = get_all_disk_usage()
    for i in res:
        print(i)
    print('磁盘负载情况')
    res = disk_io_counters()
    for i in res:
        print(i)
        
    # 磁盘使用情况：
# sdiskpart_usage(device='/dev/nvme0n1p2', mountpoint='/', fstype='ext4', total=244529655808, used=41874173952, free=190162644992, percent=18.05)
# sdiskpart_usage(device='/dev/sda', mountpoint='/raid2', fstype='ext4', total=3936913006592, used=3193023393792, free=543833485312, percent=85.45)
# sdiskpart_usage(device='/dev/nvme0n1p1', mountpoint='/boot/efi', fstype='vfat', total=535805952, used=6828032, free=528977920, percent=1.27)
# sdiskpart_usage(device='/dev/sdb2', mountpoint='/raid', fstype='fuseblk', total=2000261476352, used=1895112904704, free=105148571648, percent=94.74)
# 磁盘负载情况
# disk_io_counters_type(name='nvme0n1', reads=601391, writes=2737733, rbytes=26708477952, wbytes=53236651008, rtime=194080, wtime=8267452, reads_merged=120194, writes_merged=3210320, busy_time=8450896)
# disk_io_counters_type(name='nvme0n1p1', reads=225, writes=2, rbytes=8451072, wbytes=1024, rtime=77, wtime=5, reads_merged=4946, writes_merged=0, busy_time=224)
# disk_io_counters_type(name='nvme0n1p2', reads=601074, writes=2727395, rbytes=26698253312, wbytes=53236649984, rtime=193990, wtime=8235479, reads_merged=115248, writes_merged=3210320, busy_time=8422944)
# disk_io_counters_type(name='sda', reads=398053, writes=2868939, rbytes=144873970176, wbytes=1966131699712, rtime=3318307, wtime=82582506, reads_merged=131786, writes_merged=638850, busy_time=17615064)
# disk_io_counters_type(name='sdb', reads=1442288, writes=34513, rbytes=77510092288, wbytes=1279737856, rtime=16462439, wtime=1071008, reads_merged=0, writes_merged=278362, busy_time=14358276)
# disk_io_counters_type(name='sdb1', reads=60, writes=0, rbytes=2146304, wbytes=0, rtime=374, wtime=0, reads_merged=0, writes_merged=0, busy_time=360)
# disk_io_counters_type(name='sdb2', reads=1402794, writes=34074, rbytes=77493579776, wbytes=1279737856, rtime=16356504, wtime=1026322, reads_merged=0, writes_merged=278362, busy_time=14173752)
