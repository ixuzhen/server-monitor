import requests
import time
from getInfo.getGPUInfo import GetGPUInfo
from util.network_util import get_host_ip
from entity.message import Message
import json
from entity.GPUProcInfo import GPUProcInfo

server_address = 'http://125.216.243.36:8080/client'


def send_gpu_info():
    get_gpu_info = GetGPUInfo()
    results = get_gpu_info.getAllGPUInfo()
    ip = get_host_ip()
    date = int(time.time())
    message = Message(ip, date)
    message.addGPUIfo(results)
    js = json.dumps(message.__dict__)
    print(js)
    # headers中添加上content-type这个参数，指定为json格式
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    try:
        # post的时候，将data字典形式的参数用json包转换成json格式。
        response = requests.post(url=server_address + '/gpu/info', data=js, timeout=1)
    except requests.exceptions.ConnectTimeout:
        print('GPU 发送信息超时')


def send_gpu_proc_info():
    get_gpu_info = GetGPUInfo()
    gpus_proc_infos = get_gpu_info.get_gpu_proc_info()
    ip = get_host_ip()
    date = int(time.time())
    message = Message(ip, date)
    # 发送送 GPU 进程信心的名字
    message.gpu_proc_info = []
    for gpu_proc_infos in gpus_proc_infos:
        for gpu_proc_info in gpu_proc_infos:
            # [0, 1193282, '/raid2/xz/', '/raid/anaconda3/bin/python3.', 'C', 'python', 8005]
            message.gpu_proc_info.append(GPUProcInfo(gpu_proc_info[0], gpu_proc_info[1], gpu_proc_info[2],
                                                     gpu_proc_info[3], gpu_proc_info[4], gpu_proc_info[5],
                                                     gpu_proc_info[6]).__dict__)
    js = json.dumps(message.__dict__)
    print(js)
    # headers中添加上content-type这个参数，指定为json格式
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    try:
        # post的时候，将data字典形式的参数用json包转换成json格式。
        response = requests.post(url=server_address + '/gpu/proc/info', data=js, timeout=1)
    except requests.exceptions.ConnectTimeout:
        print('GPU 发送信息超时')
    print(response.text)


if __name__ == '__main__':
    # send_gpu_info()
    send_gpu_proc_info()
