package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.entity.Memory;
import cn.luckynow.monitoringserver.mapper.MemoryMapper;
import cn.luckynow.monitoringserver.service.IMemoryService;
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
 * @since 2023-02-28
 */
@Service
public class MemoryServiceImpl extends ServiceImpl<MemoryMapper, Memory> implements IMemoryService {

    @Override
    public Memory getNewestMemoryByIp(String ip) {
        QueryWrapper<Memory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .orderByDesc("date")
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Timestamp getNewestDateMemoryByIp(String ip) {
        Memory newestMemory = this.getNewestMemoryByIp(ip);
        return newestMemory.getDate();
    }

}
