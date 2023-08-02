from util.comment_util import sh
import os
import re
procfs_path = '/proc'


def get_cwd(pid):
    try:
        path = readlink("%s/%s/cwd" % (procfs_path, pid))
    except PermissionError:
        return ''
    return path

def get_start_time_by_pid(pid):
    try:
        output = sh(f'ps -o lstart -p {pid}')
        lines = output.split('\n')
        if len(lines) < 2:
            return ''
        return lines[1].strip()
    except Exception as e:
        print(e)
        return ''

def get_exe(pid):
    try:
        path = readlink("%s/%s/exe" % (procfs_path, pid))
    except PermissionError:
        return ''
    return path


def readlink(path):
    assert isinstance(path, str), path
    path = os.readlink(path)
    return path

def pids():
    # 得到当前系统所有的正在运行的进程
    return [int(x) for x in os.listdir(b'/proc') if x.isdigit()]


def pid_exists_posix(pid):
    """Check whether pid exists in the current process table."""
    if pid == 0:
        return True
    try:
        # If signalnum is 0, then no signal is sent, but error checking is still performed
        # this can be used to check if the target thread is still running.
        os.kill(pid, 0)
    except ProcessLookupError:
        return False
    except PermissionError:
        return True
    else:
        return True

def pid_exists(pid):
    if not pid_exists_posix(pid):
        return False
    else:
        # Linux's apparently does not distinguish between PIDs and TIDs
        # (thread IDs).
        # listdir("/proc") won't show any TID (only PIDs) but
        # os.stat("/proc/{tid}") will succeed if {tid} exists.
        # os.kill() can also be passed a TID. This is quite confusing.
        # In here we want to enforce this distinction and support PIDs
        try:
            path = f'/proc/{pid}/status'
            with open(path,'rb') as f:
                for line in f:
                    if line.startswith(b"Tgid:"):
                        tgid = int(line.split()[1])
                        return tgid == pid
                raise ValueError("'Tgid' line not found in %s" % path)
        except (EnvironmentError, ValueError):
            return pid in pids()

if __name__ == '__main__':
    res = pid_exists(1)
    print(res)