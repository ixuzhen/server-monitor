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
+ [x] 内存信息
+ [x] GPU 信息
+ [x] GPU 进程信息
+ [x] 未使用的 GPU
+ [x] 硬盘信息
+ [x] 服务器是否断连
+ [x] 开放的端口信息
+ [x] 消息推送功能
+ [x] 硬盘不足报警
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


## 部署
### 基于 Docker 进行部署
1. clone 该项目，根据自己需要修改配置文件 docker-compose.yaml 文件


```
version: "3"

services:
  monitor_springboot:
#    image: monitor:0.0.1
    build:
      context: ./
      dockerfile: Dockerfile_SpringBoot
    container_name: monitor
    ports:
      - "8080:8080"
    networks:
      - monitor_net
    depends_on:
#      - redis
      - mysql_monitor
    command:
      - --spring.datasource.url=jdbc:mysql://mysql_monitor:3306/monitoring?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false
      ## MySQL 用户名
      - --spring.datasource.username=root
      # MySQL 的密码，请保证与下方 MYSQL_ROOT_PASSWORD 的变量值一致。
      - --spring.datasource.password=123456
      
      # 发生消息的邮箱，验证码的邮箱
      - --spring.mail.username=xuzhe
      # 发生消息的邮箱，验证码的密码
      - --spring.mail.password=DfFjk


  mysql_monitor:
    image: mysql:8.0.31
    container_name: mysql_monitor
    ports:
      - "3307:3306"
    volumes:
      # 数据挂载
      - /root/mysql/data/:/var/lib/mysql/
      # 配置挂载
      - /root/mysql/conf/:/etc/mysql/conf.d/
      # 初始化目录挂载
      - /root/mysql/init/:/docker-entrypoint-initdb.d/

    networks:
      - monitor_net
    environment:
      # 请修改此密码，并对应修改上方 monitor_springboot 服务的 spring.datasource.password 变量值
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_DATABASE=monitoring


  monitor_react:
    #    image: monitor:0.0.1
    build:
      context: ./
      dockerfile: Dockerfile_React
    container_name: react_monitor
    ports:
      - "80:80"
    networks:
      - monitor_net
    depends_on:
      - monitor_springboot

networks:
  monitor_net:
```
2. 运行命令
```
docker-compose up -d
```



## 配置
TODO
