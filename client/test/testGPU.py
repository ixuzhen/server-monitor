import pynvml
pynvml.nvmlInit() # 初始化
... # 函数调用

# 获取版本号
driver_version = pynvml.nvmlSystemGetDriverVersion()
num_gpu = pynvml.nvmlDeviceGetCount()

for i in range(1):
    i=1
    handle = pynvml.nvmlDeviceGetHandleByIndex(i)
    gpu_name = pynvml.nvmlDeviceGetName(handle)

    meminfo = pynvml.nvmlDeviceGetMemoryInfo(handle)
    print(meminfo.total / 1024 / 1024)  # 显卡总的显存大小,6442450944Bit
    print(meminfo.used / 1024 /1024)  # 显存使用大小,4401950720Bit
    print(meminfo.free / 1024 /1024)  # 显卡剩余显存大小,2040500224Bit
    # meminfo.used / 1024 / 1024 = 4198
    print(pynvml.nvmlDeviceGetArchitecture(handle))
    # 得到电源额定功率 260w
    print(pynvml.nvmlDeviceGetEnforcedPowerLimit(handle))
    print(pynvml.nvmlDeviceGetPowerUsage(handle))
    # 风扇
    print(pynvml.nvmlDeviceGetFanSpeed(handle))
    # 显卡名子
    print(pynvml.nvmlDeviceGetName(handle))
    #
    print("利用率")
    print(pynvml.nvmlDeviceGetUtilizationRates(handle))
    print('温度')
    print(pynvml.nvmlDeviceGetTemperature(handle, 0))


pynvml.nvmlShutdown() # 最后要关闭管理工具