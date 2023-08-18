/*
SQLyog Ultimate - MySQL GUI v8.2 
MySQL - 8.0.31 : Database - monitoring
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`monitoring` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `monitoring`;

/*Table structure for table `disk_usage` */

DROP TABLE IF EXISTS `disk_usage`;

CREATE TABLE `disk_usage` (
  `disk_usage_id` bigint NOT NULL AUTO_INCREMENT,
  `host_id` bigint DEFAULT NULL,
  `ip` varchar(50) NOT NULL,
  `date_disk` timestamp NULL DEFAULT NULL,
  `device` varchar(50) DEFAULT NULL,
  `mountpoint` varchar(50) DEFAULT NULL,
  `total` bigint DEFAULT NULL,
  `free` bigint DEFAULT NULL,
  `used` bigint DEFAULT NULL,
  `percent` float DEFAULT NULL,
  `fstype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`disk_usage_id`),
  KEY `NewIndex1` (`ip`,`date_disk`)
) ENGINE=InnoDB AUTO_INCREMENT=66863 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `docker_info` */

DROP TABLE IF EXISTS `docker_info`;

CREATE TABLE `docker_info` (
  `id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '容器名称',
  `container_id` varchar(50) DEFAULT NULL COMMENT '容器id',
  `cpu` varchar(20) DEFAULT NULL,
  `memery_usage` varchar(20) DEFAULT NULL,
  `memery_limit` varchar(20) DEFAULT NULL,
  `memery_usage_rate` varchar(20) DEFAULT NULL,
  `net_io_send` varchar(20) DEFAULT NULL COMMENT '网络发送量',
  `net_io_receive` varchar(20) DEFAULT NULL COMMENT '网络接收量',
  `block_io_read` varchar(20) DEFAULT NULL COMMENT '磁盘读取量',
  `block_io_write` varchar(20) DEFAULT NULL COMMENT '磁盘写入量',
  `ip` varchar(150) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `NewIndex2` (`ip`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `gpu_info` */

DROP TABLE IF EXISTS `gpu_info`;

CREATE TABLE `gpu_info` (
  `id_gpu_info` bigint NOT NULL AUTO_INCREMENT,
  `id_host` bigint NOT NULL,
  `ip` varchar(30) NOT NULL,
  `index_gpu` int NOT NULL,
  `count_gpu` int NOT NULL,
  `date_gpu` timestamp NOT NULL,
  `name_gpu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `driver_version` varchar(30) DEFAULT NULL,
  `memory_total` int DEFAULT NULL,
  `memory_used` int DEFAULT NULL,
  `memory_free` int DEFAULT NULL,
  `enforced_power_limit` int DEFAULT NULL,
  `power_usage` int DEFAULT NULL,
  `fan_speed` int DEFAULT NULL,
  `utilization_rate` int DEFAULT NULL,
  `temperature` int DEFAULT NULL,
  PRIMARY KEY (`id_gpu_info`),
  KEY `NewIndex1` (`ip`,`date_gpu`)
) ENGINE=InnoDB AUTO_INCREMENT=56426 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `gpu_proc` */

DROP TABLE IF EXISTS `gpu_proc`;

CREATE TABLE `gpu_proc` (
  `id_gpu_proc_info` bigint NOT NULL AUTO_INCREMENT COMMENT '每一条 GPU 进程信息的 id',
  `id_host` bigint DEFAULT NULL COMMENT '主机 id',
  `ip` varchar(30) NOT NULL COMMENT '主机 ip',
  `index_gpu` int NOT NULL COMMENT 'GPU 编号',
  `pid` bigint NOT NULL COMMENT '进程的pid',
  `type_proc` varchar(30) DEFAULT NULL COMMENT '进程类型',
  `name_proc` varchar(100) DEFAULT NULL COMMENT '进程名字',
  `memory_used` int DEFAULT NULL COMMENT '显存用量',
  `cwd` varchar(100) DEFAULT NULL COMMENT '当前工作目录',
  `exe` varchar(100) DEFAULT NULL COMMENT '执行的命令',
  `date_gpu_proc` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '进程开始时间',
  PRIMARY KEY (`id_gpu_proc_info`),
  KEY `NewIndex1` (`ip`,`date_gpu_proc`)
) ENGINE=InnoDB AUTO_INCREMENT=105299 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `host` */

DROP TABLE IF EXISTS `host`;

CREATE TABLE `host` (
  `id_host` bigint NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL,
  `date_host` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_host`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `memory` */

DROP TABLE IF EXISTS `memory`;

CREATE TABLE `memory` (
  `id_memory` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '内存信息的主键',
  `id_host` bigint DEFAULT NULL COMMENT '主机的id',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主机ip',
  `total` bigint DEFAULT NULL COMMENT '总内存',
  `available` bigint DEFAULT NULL COMMENT '剩余内存',
  `percent` double DEFAULT NULL COMMENT '使用百分比',
  `used` bigint DEFAULT NULL,
  `free` bigint DEFAULT NULL,
  `active` bigint DEFAULT NULL,
  `inactive` bigint DEFAULT NULL,
  `buffers` bigint DEFAULT NULL,
  `cached` bigint DEFAULT NULL,
  `shared` bigint DEFAULT NULL,
  `slab` bigint DEFAULT NULL,
  `date` timestamp NOT NULL,
  PRIMARY KEY (`id_memory`),
  KEY `NewIndex1` (`ip`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=18895 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `message_user` */

DROP TABLE IF EXISTS `message_user`;

CREATE TABLE `message_user` (
  `id` bigint unsigned NOT NULL COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推送使用的用户名,唯一',
  `token` varchar(50) DEFAULT NULL COMMENT '推送使用的token',
  `channel` int DEFAULT '1' COMMENT '推送方式',
  `email` varchar(50) DEFAULT NULL COMMENT '要推送的邮箱',
  `ding_webhook_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '钉钉群机器人的webhook的url',
  `ding_webhook_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '钉钉群机器人的webhook的secret',
  `feishu_webhook_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '飞书群机器人的webhook的url',
  `feishu_webhook_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '飞书群机器人的webhook的secret',
  `wechat_webhook_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '企业微信群机器人的webhook的url',
  PRIMARY KEY (`id`),
  KEY `NewIndex1` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `port` */

DROP TABLE IF EXISTS `port`;

CREATE TABLE `port` (
  `id_port` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '端口信息的id',
  `id_host` bigint DEFAULT NULL,
  `ip` varchar(130) NOT NULL COMMENT 'IP地址',
  `date` timestamp NOT NULL COMMENT '更新时间',
  `local_address` varchar(130) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '本地地址',
  `local_port` bigint DEFAULT NULL COMMENT '本地端口',
  `remote_address` varchar(130) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '远程地址',
  `remote_port` bigint DEFAULT NULL COMMENT '远程端口',
  `state` bigint DEFAULT NULL COMMENT 'socket状态',
  `inode` bigint DEFAULT NULL COMMENT 'socket对应的inode',
  `pid` bigint DEFAULT '-1' COMMENT '对应的进程id',
  `pname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '对应的进程名称',
  `type` varchar(20) DEFAULT NULL COMMENT '连接类型（tcp，udp）',
  PRIMARY KEY (`id_port`),
  KEY `NewIndex1` (`ip`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=472982 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `phonenumber` varchar(32) DEFAULT NULL COMMENT '手机号',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `user_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
  `create_by` bigint DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`),
  KEY `NewIndex1` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

/*Table structure for table `warning_link` */

DROP TABLE IF EXISTS `warning_link`;

CREATE TABLE `warning_link` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `link` varchar(50) NOT NULL COMMENT '推送链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
