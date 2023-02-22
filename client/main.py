import requests
import time
from getInfo.getGPUInfo import GetGPUInfo
from util.network_util import get_host_ip
from entity.message import Message
import json
from entity.GPUProcInfo import GPUProcInfo
from util.disk_util import get_all_disk_usage, disk_io_counters
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime
import time

server_address = 'http://125.216.243.36:8080/client'

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

def get_disk_io_counters():
    disk_counters = disk_io_counters()
    res = []
    for counter in disk_counters:
        res.append(dict(counter._asdict()))
    return res

def send_all_message():
    # 通用信息
    ip = get_host_ip()
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





if __name__ == '__main__':
    
    # scheduler = BlockingScheduler()
    # # scheduler.add_job(send_all_message, "interval", minutes=5)
    # scheduler.add_job(send_all_message, "interval", seconds=5)
    # scheduler.start()
    
    send_all_message()
