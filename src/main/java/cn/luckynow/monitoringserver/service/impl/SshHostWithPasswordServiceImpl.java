package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.SshHostWithPassword;
import cn.luckynow.monitoringserver.mapper.SshHostWithPasswordMapper;
import cn.luckynow.monitoringserver.service.ISshHostWithPasswordService;
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
public class SshHostWithPasswordServiceImpl extends ServiceImpl<SshHostWithPasswordMapper, SshHostWithPassword> implements ISshHostWithPasswordService {


    @Override
    public String getPasswordByHostId(Integer hostId) {
        QueryWrapper<SshHostWithPassword> wrapper = new QueryWrapper<>();
        wrapper.eq("id", hostId);
        SshHostWithPassword host = this.getOne(wrapper);
        return host.getPassword();
    }


}
