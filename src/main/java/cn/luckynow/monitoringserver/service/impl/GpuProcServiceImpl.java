package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.GpuProc;
import cn.luckynow.monitoringserver.mapper.GpuProcMapper;
import cn.luckynow.monitoringserver.service.IGpuProcService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
