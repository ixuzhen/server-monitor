package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.entity.GpuProc;
import cn.luckynow.monitoringserver.mapper.GpuProcMapper;
import cn.luckynow.monitoringserver.service.IGpuProcService;
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
 * @since 2022-12-06
 */
@Service
public class GpuProcServiceImpl extends ServiceImpl<GpuProcMapper, GpuProc> implements IGpuProcService {

    @Override
    public GpuProc getNewestOneGpuProcByIp(String ip) {
        QueryWrapper<GpuProc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip",ip)
                .orderByDesc("date_gpu_proc")
                .last("LIMIT 1");
         return this.getOne(queryWrapper);
    }

    @Override
    public Timestamp getNewestDateGpuProcByIp(String ip) {
        GpuProc newestGpuProc = this.getNewestOneGpuProcByIp(ip);
        if (newestGpuProc == null) {
            return null;
        }
        return newestGpuProc.getDateGpuProc();
    }

    @Override
    public List<GpuProc> getGpuProcByIpAndDate(String ip, Timestamp date) {
        QueryWrapper<GpuProc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .eq("date_gpu_proc", date);
        return this.list(queryWrapper);
    }

    @Override
    public List<GpuProc> getNewestGpuProcListByIp(String ip) {
        Timestamp newestDateGpuProcByIp = this.getNewestDateGpuProcByIp(ip);
        return this.getGpuProcByIpAndDate(ip, newestDateGpuProcByIp);
    }
}
