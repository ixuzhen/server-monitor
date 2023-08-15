package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.GpuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2022-12-02
 */
public interface IGpuInfoService extends IService<GpuInfo> {

    int getGPUCount(String ip);
    public GpuInfo getRandomGPUInfoByIp(String ip) ;

    GpuInfo getNewestOneGpuInfoByIp(String ip);
    Timestamp getNewestDateGpuByIp(String ip);
    List<GpuInfo> getGpuInfoByIpAndDate(String ip, Timestamp date);
    List<GpuInfo> getNewestGpuInfoListByIp(String ip);

}
