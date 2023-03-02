package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.Memory;
import cn.luckynow.monitoringserver.mapper.MemoryMapper;
import cn.luckynow.monitoringserver.service.IMemoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
