
class GPUInfo:
    def __init__(self, countGpu, driverVersion, indexGpu, nameGpu,memoryTotal,
                memoryUsed,memoryFree,enforcedPowerLimit,powerUsage,
                fanSpeed, utilizationRate,temperature):
        self.countGpu = countGpu
        self.driverVersion=driverVersion
        self.indexGpu = indexGpu
        self.nameGpu = nameGpu
        self.memoryTotal = memoryTotal
        self.memoryUsed = memoryUsed
        self.memoryFree = memoryFree
        self.enforcedPowerLimit = enforcedPowerLimit
        self.powerUsage = powerUsage
        self.fanSpeed = fanSpeed
        self.utilizationRate = utilizationRate
        self.temperature = temperature

# 经过 getGPUInfo 实际得到的数据类型
# count:  2       <class 'int'>
# driver_version: 470.141.03      <class 'str'>
# index:  1       <class 'int'>
# name:   NVIDIA GeForce RTX 2080 Ti      <class 'str'>
# memory_toal:    11019   <class 'int'>
# memory_used:    7831    <class 'int'>
# memory_free:    3187    <class 'int'>
# enforced_power_limit:   250     <class 'int'>
# power_usage:    184     <class 'int'>
# fan_speed:      90      <class 'int'>
# utilization_rate:       48      <class 'int'>
# temperature:    78      <class 'int'>
