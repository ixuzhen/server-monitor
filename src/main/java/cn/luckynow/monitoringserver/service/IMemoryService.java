package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.entity.Memory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-02-28
 */
public interface IMemoryService extends IService<Memory> {



    Memory getNewestMemoryByIp(String ip);
    Timestamp getNewestDateMemoryByIp(String ip);

}
