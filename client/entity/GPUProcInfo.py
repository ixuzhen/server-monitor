

class GPUProcInfo:

    def __init__(self,
            indexGpu,
            pid,
            cwd,
            exe,
            typeProc,
            nameProc,
            memoryUsed,
            startTime
                 ):
        self.indexGpu = indexGpu
        self.pid = pid
        self.typeProc = typeProc
        self.nameProc = nameProc
        self.memoryUsed = memoryUsed
        self.cwd = cwd
        self.exe = exe
        self.startTime = startTime


