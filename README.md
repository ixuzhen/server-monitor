<p align="center">
  <a href="https://github.com/xz2048/server-monitor"><img src="https://raw.githubusercontent.com/xz2048/server-monitor/main/web/public/logo192.png" width="150" height="150" alt="gin-template logo"></a>
</p>

<div align="center">

# 服务器监控系统

_✨ 基于 SpringBoot & React 的服务器监控系统 ✨_

</div>

<p align="center">
  <a href="https://raw.githubusercontent.com/xz2048/server-monitor/main/LICENSE">
    <img src="https://img.shields.io/github/license/xz2048/server-monitor?color=brightgreen" alt="license">
  </a>
  <a href="https://github.com/xz2048/server-monitor/releases/latest">
    <img src="https://img.shields.io/github/v/release/xz2048/server-monitor?color=brightgreen&include_prereleases" alt="release">
  </a>
  <a href="https://github.com/xz2048/server-monitor/releases/latest">
    <img src="https://img.shields.io/github/downloads/xz2048/server-monitor/total?color=brightgreen&include_prereleases" alt="release">
  </a>
</p>

<p align="center">
  <a href="https://github.com/xz2048/server-monitor/releases">程序下载</a>
  ·
  <a href="https://github.com/xz2048/server-monitor#部署">部署教程</a>
  ·
  <a href="https://github.com/xz2048/server-monitor/issues">意见反馈</a>
  ·
  <a href="">在线演示</a>
</p>

## 代办
+ [ ] 一开始就发送心跳
+ [ ] 增加进程的开始时间
+ [ ] docker 容器的监控
+ [x] 内存信息
+ [x] GPU 信息
+ [x] GPU 进程信息
+ [x] 未使用的 GPU
+ [x] 硬盘信息
+ [x] 服务器是否断连
+ [x] 开放的端口信息
+ [x] 消息推送功能
+ [x] 硬盘不足报警
+ [ ] SSH 的版本
+ [ ] CPU 信息
+ [ ] 网络负载信息
+ [ ] 显卡空闲提醒
+ [ ] 远程终端
+ [ ] 内存，CPU历史记录
+ [ ] 更加详细的进程信息，如进程开始时间
+ [ ] 重要日志的信息，如登录日志
+ [ ] server 端提供下载上传数据脚本的功能
+ [ ] 清理数据 server 端数据库的功能
+ [ ] 远程文件管理

## 主要功能
1. 监控服务器的各项信息，和其他监控项目的不同是可以监控 GPU 的信息，适合实验室管理深度学习服务器的人员。
2. 消息推送，通过统一的 api 接口整合了企业微信、钉钉、飞书和邮件的推送功能。
3. 如果配置了上边的消息推送功能，如果发送服务器断连、硬盘不足、GPU 空闲等情况会发送邮件提醒（也可以通过企业微信、钉钉、飞书推送）。

## 展示
<img src="./images/diskInfo.png">
<img src="./images/gpuInfo.png">
<img src="images/connectInfo.png">

## 部署
### 基于 Docker 进行部署接收端
1. clone 该项目
```shell
git clone https://github.com/ixuzhen/server-monitor.git
cd server-monitor/
```
2. 修改 Dockerfile_React 中的服务器地址
```shell
vim ./Dockfile_React
```

```dockerfile
FROM node:18.12.1 AS build
#......
# 改成自己服务器地址
ENV REACT_APP_SERVER=https://luckynow.cn:8080
#......
CMD ["nginx", "-g", "daemon off;"]
```
3. 运行命令
```shell
# Docker Compose版本1.27.0及更高版本中使用下面这个命令
docker compose up -d
# Docker Compose版本1.27.0以下使用下面这个命令
docker-compose up -d
```

4. 停止容器
```shell
docker-compose stop
```

5. 其他
如果在执行 `docker compose up -d` 时很慢，卡在` => [builder 5/5] RUN cd /build && mvn -B -ntp -DskipTests package`
这里，可以把打好的 jar 包放在 `./target/` 文件夹下，并将 jar 包改名为 `monitoring-server.jar`。
```shell
# 改名
mv ./target/monitoring-server-0.0.1-SNAPSHOT.jar ./target/monitoring-server.jar
```

### 部署发送端
1. clone 该项目
```shell
git clone https://github.com/ixuzhen/server-monitor.git
cd ./server-monitor/client
```
2. 修改配置文件 config.yaml, 必须修改第一个，修改成自己的接收端服务器地址
```
# 中心服务器地址
server_address: http://225.216.243.111:8080
# 发送本机服务器信息的间隔时间，单位是秒
send_message_interval: 600
# 发送心跳包的间隔时间，单位是秒
send_hearbeat_interval: 60
```
3. 安装依赖并运行
```
# python 大于3.6
pip install pynvml;
pip install apscheduler;
pip install requests;

python main.py
```
## 配置
TODO
