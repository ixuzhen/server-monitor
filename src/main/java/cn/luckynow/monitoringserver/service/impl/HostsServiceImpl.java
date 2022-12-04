package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.mapper.HostsMapper;
import cn.luckynow.monitoringserver.service.IHostsService;
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
public class HostsServiceImpl extends ServiceImpl<HostsMapper, Host> implements IHostsService {

}
