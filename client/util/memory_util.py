from collections import namedtuple

memory_type = namedtuple(
    'memory_type', ['total', 'available', 'percent', 'used', 'free',
              'active', 'inactive', 'buffers', 'cached', 'shared', 'slab'])



def get_memory():
    memorys = {}
    with open('/proc/meminfo', "rt") as f:
        for line in f:
            fields = line.strip().split()
            memorys[fields[0]] = int(fields[1]) * 1024
            
    total = memorys['MemTotal:']
    free = memorys['MemFree:']

    buffers = memorys.get('Buffers:', 0)
    cached = memorys.get('Cached:', 0)
    cached += memorys.get('SReclaimable:', 0)
    try:
        shared = memorys['Shmem:']  # since kernel 2.6.32
    except KeyError:
        shared = memorys.get('MemShared:', 0)  # kernels 2.4
    active = memorys.get('Active:', 0)
    inactive = memorys.get('Inactive:',0)
    slab = memorys.get('Slab:',0)
    used = total - free - cached - buffers
    if used < 0:
        used = total - free
    avail = memorys.get('MemAvailable:',0)
    # 使用百分比
    percent = round(float(total - avail) / total * 100,2)

    return memory_type(total, avail, percent, used,free,
                       active, inactive,buffers, cached, shared,slab)
    



    
            
if __name__ == '__main__':
    print(get_memory())