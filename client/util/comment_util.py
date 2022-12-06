import subprocess
import shlex

GLOBAL_TIMEOUT = 5


def sh(cmd):
    """run cmd in a subprocess and return its output.
    raises RuntimeError on error.
    """
    if isinstance(cmd, str):
        cmd = shlex.split(cmd)
    p = subprocess.Popen(cmd, shell=False, stdout=subprocess.PIPE,
                         stderr=subprocess.PIPE, universal_newlines=True)
    stdout, stderr = p.communicate(timeout=GLOBAL_TIMEOUT)
    if p.returncode != 0:
        raise RuntimeError(stderr)
    if stderr:
        print(stderr)
    if stdout.endswith('\n'):
        stdout = stdout[:-1]
    return stdout


if __name__ == '__main__':
    print(sh(["ls"]))
