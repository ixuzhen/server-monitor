import requests
import time
from getInfo.getGPUInfo import GetGPUInfo
from util.memory_util import get_memory
from util.netstat import get_TCP_UDP_with_pid
from util.network_util import get_host_ip
from entity.message import Message
import json
from entity.GPUProcInfo import GPUProcInfo
from util.disk_util import get_all_disk_usage, disk_io_counters
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime
import time
import yaml

# 获取本机ip
ip = get_host_ip()
# 指定配置文件路径
config_file_path = './config.yaml'

# 读取YAML文件
with open(config_file_path, 'r') as file:
    config_data = yaml.safe_load(file)

# 现在，config_data变量将包含YAML文件中的配置数据
# 可以通过字典键值来访问配置信息，例如：
server_address = config_data['server_address'] + '/client'
# server_address = 'http://125.216.243.40:8080/client'

send_message_interval = config_data['send_message_interval']
send_hearbeat_interval = config_data['send_hearbeat_interval']



# TypeError: Object of type 'bytes' is not JSON serializable
# 因为有上边这个错误，所以加这么一个类
class MyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, bytes):
            return str(obj, encoding='utf-8');
        return json.JSONEncoder.default(self, obj)


def send_gpu_info():
    get_gpu_info = GetGPUInfo()
    results = get_gpu_info.getAllGPUInfo()
    ip = get_host_ip()
    date = int(time.time())
    message = Message(ip, date)
    message.gpuInfo = results
    # message.addGPUIfo(results)
    js = json.dumps(message.__dict__)
    # headers中添加上content-type这个参数，指定为json格式
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    try:
        # post的时候，将data字典形式的参数用json包转换成json格式。
        response = requests.post(url=server_address + '/gpu/info', data=js, timeout=1)
    except requests.exceptions.ConnectTimeout:
        print('GPU 发送信息超时')
    


def send_gpu_proc_info():
    ip = get_host_ip()
    date = int(time.time())
    message = Message(ip, date)
    # 发送送 GPU 进程信心的名字
    message.gpu_proc_info = get_gpu_proc_info()
    js = json.dumps(message.__dict__)
    # js = MyEncoder().default(message.__dict__)
    # print(js)
    # headers中添加上content-type这个参数，指定为json格式
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    try:
        # post的时候，将data字典形式的参数用json包转换成json格式。
        response = requests.post(url=server_address + '/gpu/proc/info', data=js, timeout=1)
    except requests.exceptions.ConnectTimeout:
        print('GPU 发送信息超时')
    print(response.text)


def get_gpu_proc_info():
    get_gpu_info = GetGPUInfo()
    gpus_proc_infos = get_gpu_info.get_gpu_proc_info()
    results = []
    for gpu_proc_infos in gpus_proc_infos:
        for gpu_proc_info in gpu_proc_infos:
            # [0, 1193282, '/raid2/xz/', '/raid/anaconda3/bin/python3.', 'C', 'python', 8005]
            results.append(GPUProcInfo(gpu_proc_info[0], gpu_proc_info[1], gpu_proc_info[2],
                                                     gpu_proc_info[3], gpu_proc_info[4], gpu_proc_info[5],
                                                     gpu_proc_info[6]).__dict__)
    return results
    
def get_gpu_common_info():
    # GPU 信息
    get_gpu_info = GetGPUInfo()
    gpu_common_info = get_gpu_info.getAllGPUInfo()
    return gpu_common_info

def get_disk_usage():
    usages = get_all_disk_usage()
    res = []
    for usage in usages:
        res.append(dict(usage._asdict()))
    return res

def get_TCP_UDP_with_pid_dict():
    infos = get_TCP_UDP_with_pid()
    res = []
    for info in infos:
        res.append(dict(info._asdict()))
    return res

def get_disk_io_counters():
    disk_counters = disk_io_counters()
    res = []
    for counter in disk_counters:
        res.append(dict(counter._asdict()))
    return res

def send_all_message():
    # 通用信息
    
    date = int(time.time())
    message = Message(ip, date)
    # GPU 信息
    message.gpu_info = get_gpu_common_info()
    # GPU 进程信息
    message.gpu_proc_info = get_gpu_proc_info()
    # 硬盘用量
    disk_usages = get_disk_usage()
    message.disk_usages = disk_usages
    # 硬盘读写平均速度
    disk_counters = get_disk_io_counters()
    message.disk_counters = disk_counters
    # 内存信息
    memory = get_memory()
    message.memory = dict(memory._asdict())
    # 开放的端口信息
    ports = get_TCP_UDP_with_pid_dict()
    message.ports = ports
    js = json.dumps(message.__dict__,cls=MyEncoder)
    # print(js)
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    try:
        # post的时候，将data字典形式的参数用json包转换成json格式。
        response = requests.post(url=server_address + '/data', data=js, timeout=1)
    except BaseException as err:
        print(f'error: {err}')
    else:
        print(response.text)

def send_heartbeat():
    # 通用信息
    
    date = int(time.time())
    message = Message(ip, date)
    message_json = json.dumps(message.__dict__,cls=MyEncoder)
    # print(js)
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    try:
        # post的时候，将data字典形式的参数用json包转换成json格式。
        response = requests.post(url=server_address + '/heartbeat', data=message_json, timeout=1)
    except BaseException as err:
        print(f'error: {err}')
    else:
        print(response.text)


def start():
    scheduler = BlockingScheduler()
    # 定时发送所以信息
    scheduler.add_job(send_all_message, "interval", seconds=send_message_interval)
    # 定时发送心跳
    scheduler.add_job(send_heartbeat, "interval", seconds=send_hearbeat_interval)
    scheduler.start()


if __name__ == '__main__':
    start()

