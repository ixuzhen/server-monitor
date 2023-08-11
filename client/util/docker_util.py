from util.comment_util import sh
from entity.DockerInfo import DockerInfo


def get_docker_info():
    output = sh(f'docker stats --no-stream')
    lines = output.split('\n')
    res = []
    for i, line in enumerate(lines):
        if i == 0:
            continue
        line = line.strip()
        line_split = line.split(' ')
        line_split = [i for i in line_split if (i != '' and i != ' ' and i !='/')]
        #  将得到的line_split转化为DockerInfo对象,按照顺序,一共11个参数
        info = DockerInfo(line_split[0],line_split[1],line_split[2],line_split[3],line_split[4],
                   line_split[5],line_split[6],line_split[7],line_split[8],line_split[9],line_split[10])
        res.append(info)
        # print(info.toString())
    return res

if __name__ == '__main__':
    print(get_docker_info())