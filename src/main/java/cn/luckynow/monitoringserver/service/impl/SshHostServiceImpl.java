package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.SshHost;
import cn.luckynow.monitoringserver.mapper.SshHostMapper;
import cn.luckynow.monitoringserver.service.ISshHostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xz
 * @since 2023-08-28
 */
@Service
public class SshHostServiceImpl extends ServiceImpl<SshHostMapper, SshHost> implements ISshHostService {

}
