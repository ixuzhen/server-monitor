
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime

from main import get_disk_io_counters
from util.memory_util import get_memory
from util.process_util import get_start_time_by_pid
from main import *


def my_clock():
    print("Hello! The time is:%s"%datetime.now())


def test_memory():
    info = get_memory()

    print(info)
    print(dict(info._asdict()))

def test_disk():
    disk_counters = get_disk_io_counters()
    print(disk_counters)

def test_get_start_time_by_pid():
    get_start_time_by_pid(347306)

if __name__ == '__main__':
    # test_memory()
    # test_disk()
    # test_get_start_time_by_pid()
    res = get_gpu_proc_info()
    print(res)