import requests
import time
from getInfo.getGPUInfo import GetGPUInfo
from util.networkUtil import get_host_ip
from entity.message import Message
import json

server_address = 'http://125.216.243.36:8080/client'


def send_gpu_info():
    getGPUInfo = GetGPUInfo()
    results = getGPUInfo.getAllGPUInfo()
    ip = get_host_ip()
    # date = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    date = int(time.time())
    message = Message(ip, date)
    message.addGPUIfo(results)
    js = json.dumps(message.__dict__)
    print(js)
    # headers中添加上content-type这个参数，指定为json格式
    headers = {'Content-Type': 'application/json', 'Connection': 'close'}
    # post的时候，将data字典形式的参数用json包转换成json格式。
    response = requests.post(url= server_address + '/gpu/info', data=js)


if __name__ == '__main__':
    send_gpu_info()

