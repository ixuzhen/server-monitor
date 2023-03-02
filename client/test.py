
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime

from main import get_disk_io_counters
from util.memory_util import get_memory


def my_clock():
    print("Hello! The time is:%s"%datetime.now())


def test_memory():
    info = get_memory()

    print(info)
    print(dict(info._asdict()))

def test_disk():
    disk_counters = get_disk_io_counters()
    print(disk_counters)


if __name__ == '__main__':
    test_memory()
    test_disk()