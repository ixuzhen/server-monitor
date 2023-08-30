package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.SshHost;
import cn.luckynow.monitoringserver.entity.SshHostWithPassword;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.mapper.SshHostMapper;
import cn.luckynow.monitoringserver.service.ISshHostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<SshHost> getListByUserId(Long userId) {
        QueryWrapper<SshHost> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.list(wrapper);
    }

//    @Override
//    public String getPasswordByHostId(Integer hostId) {
//        QueryWrapper<SshHost> wrapper = new QueryWrapper<>();
//        wrapper.eq("id", hostId);
//        SshHost sshHost = this.getOne(wrapper);
//        return sshHost.getPassword();
//    }


}
