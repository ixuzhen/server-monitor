package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.entity.DockerInfo;
import cn.luckynow.monitoringserver.mapper.DockerInfoMapper;
import cn.luckynow.monitoringserver.service.IDockerInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xz
 * @since 2023-08-11
 */
@Service
public class DockerInfoServiceImpl extends ServiceImpl<DockerInfoMapper, DockerInfo> implements IDockerInfoService {

    @Override
    public int getDockerCount(String ip) {
        List<DockerInfo> list = this.getNewestDockerInfoListByIp(ip);
        return list.size();
    }

    @Override
    public DockerInfo getNewestDockerInfoByIp(String ip) {
        QueryWrapper<DockerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .orderByDesc("date")
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Timestamp getNewestDateDockerInfoByIp(String ip) {
        DockerInfo newestDockerInfo = this.getNewestDockerInfoByIp(ip);
        return newestDockerInfo!=null?newestDockerInfo.getDate():null;
    }

    @Override
    public List<DockerInfo> getDockerInfoByIpAndDate(String ip, Timestamp date) {
        QueryWrapper<DockerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .eq("date", date);
        return this.list(queryWrapper);
    }

    @Override
    public List<DockerInfo> getNewestDockerInfoListByIp(String ip) {
        Timestamp newestDate = this.getNewestDateDockerInfoByIp(ip);
        return this.getDockerInfoByIpAndDate(ip, newestDate);
    }
}
