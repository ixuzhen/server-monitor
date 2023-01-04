package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.mapper.DiskUsageMapper;
import cn.luckynow.monitoringserver.service.IDiskUsageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
