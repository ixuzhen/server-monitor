package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.DockerInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-08-11
 */
public interface IDockerInfoService extends IService<DockerInfo> {

    int getDockerCount(String ip);
    DockerInfo getNewestDockerInfoByIp(String ip);
    Timestamp getNewestDateDockerInfoByIp(String ip);
    List<DockerInfo> getDockerInfoByIpAndDate(String ip, Timestamp date);
    List<DockerInfo> getNewestDockerInfoListByIp(String ip);
}
