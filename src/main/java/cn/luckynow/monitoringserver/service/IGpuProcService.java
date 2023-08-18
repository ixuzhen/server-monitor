package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.GpuProc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2022-12-06
 */
public interface IGpuProcService extends IService<GpuProc> {

    GpuProc getNewestOneGpuProcByIp(String ip);
    Timestamp getNewestDateGpuProcByIp(String ip);
    List<GpuProc> getGpuProcByIpAndDate(String ip, Timestamp date);
    List<GpuProc> getNewestGpuProcListByIp(String ip);
}
