package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.Port;
import cn.luckynow.monitoringserver.mapper.PortMapper;
import cn.luckynow.monitoringserver.service.IPortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
