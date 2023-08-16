package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.entity.Port;
import cn.luckynow.monitoringserver.mapper.PortMapper;
import cn.luckynow.monitoringserver.service.IPortService;
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
 * @since 2023-03-02
 */
@Service
public class PortServiceImpl extends ServiceImpl<PortMapper, Port> implements IPortService {

    @Override
    public Port getNewestOnePortByIp(String ip) {
        QueryWrapper<Port> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .orderByDesc("date")
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Timestamp getNewestDatePortByIp(String ip) {
        Port newestPort = this.getNewestOnePortByIp(ip);
        if (newestPort == null) {
            return null;
        }
        return newestPort.getDate();
    }

    @Override
    public List<Port> getPortByIpAndDate(String ip, Timestamp date) {
        QueryWrapper<Port> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .eq("date", date);
        return this.list(queryWrapper);
    }

    @Override
    public List<Port> getNewestPortListByIp(String ip) {
        Timestamp newestDate = this.getNewestDatePortByIp(ip);
        return this.getPortByIpAndDate(ip, newestDate);
    }
}
