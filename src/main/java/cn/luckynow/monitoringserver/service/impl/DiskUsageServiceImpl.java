package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.mapper.DiskUsageMapper;
import cn.luckynow.monitoringserver.service.IDiskUsageService;
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
 * @since 2022-12-11
 */
@Service
public class DiskUsageServiceImpl extends ServiceImpl<DiskUsageMapper, DiskUsage> implements IDiskUsageService {


    /**
     * 得到最新的数据
     * SELECT * FROM disk_usage
     *      WHERE ip = '125.216.243.209'
     *      ORDER BY date_disk DESC
     *      LIMIT 1;
     * @param ip
     * @return
     */
    @Override
    public DiskUsage getNewestOneDiskUsageByIp(String ip) {
        QueryWrapper<DiskUsage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .orderByDesc("date_disk")
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Timestamp getNewestDateDiskByIp(String ip) {
        DiskUsage newestDiskUsage = this.getNewestOneDiskUsageByIp(ip);
        return newestDiskUsage.getDateDisk();
    }

    @Override
    public List<DiskUsage> getDiskUsageByIpAndDate(String ip, Timestamp date) {
        QueryWrapper<DiskUsage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .eq("date_disk", date);
        return this.list(queryWrapper);
    }

    @Override
    public List<DiskUsage> getNewestDiskUsageListByIp(String ip) {
        Timestamp newestDateDiskByIp = this.getNewestDateDiskByIp(ip);
        return this.getDiskUsageByIpAndDate(ip, newestDateDiskByIp);
    }


}
