package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.entity.Port;
import cn.luckynow.monitoringserver.mapper.GpuInfoMapper;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xz
 * @since 2022-12-02
 */
@Service
public class GpuInfoServiceImpl extends ServiceImpl<GpuInfoMapper, GpuInfo> implements IGpuInfoService {

    @Override
    public int getGPUCount(String ip) {
        return this.getRandomGPUInfoByIp(ip).getCountGpu();
    }

    @Override
    public GpuInfo getRandomGPUInfoByIp(String ip) {
        QueryWrapper<GpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip",ip)
                .last("LIMIT 1");
        GpuInfo gpuInfo = this.getOne(queryWrapper);
        return gpuInfo;
    }

    @Override
    public GpuInfo getNewestOneGpuInfoByIp(String ip) {
        QueryWrapper<GpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .orderByDesc("date_gpu")
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Timestamp getNewestDateGpuByIp(String ip) {
        GpuInfo newestGpuInfo = this.getNewestOneGpuInfoByIp(ip);
        return newestGpuInfo.getDateGpu();
    }

    @Override
    public List<GpuInfo> getGpuInfoByIpAndDate(String ip, Timestamp date) {
        QueryWrapper<GpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .eq("date_gpu", date);
        return this.list(queryWrapper);
    }

    @Override
    public List<GpuInfo> getNewestGpuInfoListByIp(String ip) {
        Timestamp newestDate = this.getNewestDateGpuByIp(ip);
        return this.getGpuInfoByIpAndDate(ip, newestDate);
    }
}
