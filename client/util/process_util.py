
import os

procfs_path = '/proc'


def get_cwd(pid):
    try:
        path = readlink("%s/%s/cwd" % (procfs_path, pid))
    except PermissionError:
        return ''
    return path


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


# if __name__ == '__main__':
#     path = get_cwd(1137195)
#     print(path)
#     path = get_exe(1137195)
#     print(path)