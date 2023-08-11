# https://blog.csdn.net/weixin_44191019/article/details/108377525
class DockerInfo:
    containerId = ""
    name = ""
    cpu = "0.0"
    memeryUsage = "0.0"
    memeryLimit = "0.0"
    memeryUsageRate = "0.0"
    netI0Send = '0.0MB'
    netIOReceive = '0.0MB'
    blockIORead = '0.0MB'
    blockIOWrite = '0.0MB'
    pids = 0

    # 构造函数
    def __init__(self, containerId, name, cpu, memeryUsage, memeryLimit, memeryUsageRate, netIOSend, netIOReceive,
                 blockIORead, blockIOWrite, pids):
        self.name = name
        self.containerId = containerId
        self.cpu = cpu
        self.memeryUsage = memeryUsage
        self.memeryLimit = memeryLimit
        self.memeryUsageRate = memeryUsageRate
        self.netIOSend = netIOSend
        self.netIOReceive = netIOReceive
        self.blockIORead = blockIORead
        self.blockIOWrite = blockIOWrite
        self.pids = pids

    def toString(self):
        return "containerId:" + self.containerId + ",name:" + self.name + ",cpu:" + self.cpu + ",memeryUsage:" + self.memeryUsage + ",memeryLimit:" + self.memeryLimit + ",memeryUsageRate:" + self.memeryUsageRate + ",netIOSend:" + self.netIOSend + ",netIOReceive:" + self.netIOReceive + ",blockIORead:" + self.blockIORead + ",blockIOWrite:" + self.blockIOWrite + ",pids:" + str(
            self.pids)