from util.comment_util import sh
from util.process_util import get_cwd, get_exe


def get_gpu_processes_info_by_id(index):
    # 返回一个 GPU 的进程数组，每一个进程[index, pid, cwd, exe, type, process name, gpu memory usage]
    # index 标记哪一张显卡
    res = []
    output = sh(f'nvidia-smi -q --display=PIDS --id={index}')
    lines = output.split('\n')
    list_proc = []
    for line in lines:
        line = line.strip()

        if line.startswith('Process ID'):
            pid = int(line.split()[-1])
            list_proc.clear()
            list_proc.append(index)
            list_proc.append(pid)
            list_proc.append(get_cwd(pid))
            list_proc.append(get_exe(pid))
        if line.startswith('Type'):
            list_proc.append(line.split()[-1])
        if line.startswith('Name'):
            list_proc.append(line.split()[-1])
        if line.startswith('Used GPU Memory'):
            list_proc.append(int(line.split()[-2]))
            res.append(list_proc.copy())
    return res


def get_gpu_num():
    output = sh(f'nvidia-smi -q --display=MEMORY')
    lines = output.split('\n')
    for line in lines:
        line = line.strip()
        if line.startswith('Attached GPUs'):
            return int(line.split()[-1])
    return 0

if __name__ == '__main__':
    print(get_gpu_processes_info_by_id(1))
