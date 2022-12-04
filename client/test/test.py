import sys
sys.path.append('..')
from getInfo import getGPUInfo
print(getGPUInfo.data)
print(getGPUInfo.__name__)

getGPUInfo1 = getGPUInfo.GetGPUInfo()
# result = getGPUInfo.getAllGPUInfo()
print(getGPUInfo1.a)
result = getGPUInfo1.getOneGPUInfo(0)
