import subprocess


def test_subp():
    p = subprocess.Popen("free", shell=True, stdout=subprocess.PIPE)
    output1 = p.communicate()[0].strip()
    output2 = p.communicate()[0].strip()
    print(output1)
    print(output2)





if __name__ == '__main__':
    test_subp()


