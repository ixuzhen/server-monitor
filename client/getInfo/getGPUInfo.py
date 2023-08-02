import sys

# sys.path.append('..')
from entity.GpuInfo import GPUInfo
import pynvml
from util.gpu_util import get_gpu_processes_info_by_id, get_gpu_num



class GetGPUInfo:

    def __init__(self):
        self.num_gpu = get_gpu_num()

    def getAllGPUInfo(self):
        allInfo = []
        pynvml.nvmlInit()  # 初始化
        num_gpu = pynvml.nvmlDeviceGetCount()
        pynvml.nvmlShutdown()
        for i in range(num_gpu):
            allInfo.append(self.getOneGPUInfo(i))
        return allInfo

    def byte2Mb(self, biteMemory):
        return int(biteMemory / 1024 / 1024)

    def paringPower(self, power):
        return int(power / 1000)

    def getOneGPUInfo(self, index):
        pynvml.nvmlInit()
        handle = pynvml.nvmlDeviceGetHandleByIndex(index)
        count = pynvml.nvmlDeviceGetCount()
        driver_version = pynvml.nvmlSystemGetDriverVersion()
        name = pynvml.nvmlDeviceGetName(handle)
        meminfo = pynvml.nvmlDeviceGetMemoryInfo(handle)
        memory_total = self.byte2Mb(meminfo.total)
        memory_used = self.byte2Mb(meminfo.used)
        memory_free = self.byte2Mb(meminfo.free)
        enforced_power_limit = self.paringPower(pynvml.nvmlDeviceGetEnforcedPowerLimit(handle))
        power_usage = self.paringPower(pynvml.nvmlDeviceGetPowerUsage(handle))
        fan_speed = pynvml.nvmlDeviceGetFanSpeed(handle)
        utilization_rate_all = pynvml.nvmlDeviceGetUtilizationRates(handle)
        utilization_rate = utilization_rate_all.gpu

        temperature = pynvml.nvmlDeviceGetTemperature(handle, 0)

        gpuInfo = GPUInfo(count, driver_version, index, name, memory_total,
                          memory_used, memory_free, enforced_power_limit, power_usage,
                          fan_speed, utilization_rate, temperature)
        pynvml.nvmlShutdown()
        return gpuInfo.__dict__

    def get_gpu_proc_info(self):
        res = []
        for i in range(self.num_gpu):
            res.append(get_gpu_processes_info_by_id(i))
        return res
