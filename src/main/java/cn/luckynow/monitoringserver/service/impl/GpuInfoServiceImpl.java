package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.mapper.GpuInfoMapper;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
