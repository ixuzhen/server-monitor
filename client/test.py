
import requests
import time
from getInfo.getGPUInfo import GetGPUInfo
from util.network_util import get_host_ip
from entity.message import Message
import json
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
response = requests.post(url='http://125.216.243.36:8080/gpu/info', data=js)
# response = requests.get(url='http://222.201.190.214:8080/test02')
print(response.text)
