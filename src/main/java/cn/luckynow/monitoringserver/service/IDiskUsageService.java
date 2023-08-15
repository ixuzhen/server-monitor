package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2022-12-11
 */
public interface IDiskUsageService extends IService<DiskUsage> {

    /**
     * 得到最新的数据
     * @param ip
     * @return
     */
    DiskUsage getNewestOneDiskUsageByIp(String ip);

    Timestamp getNewestDateDiskByIp(String ip);

    List<DiskUsage> getDiskUsageByIpAndDate(String ip, Timestamp date);

    List<DiskUsage> getNewestDiskUsageListByIp(String ip);

}
