package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.SshHost;
import cn.luckynow.monitoringserver.entity.SshHostWithPassword;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-08-28
 */
public interface ISshHostWithPasswordService extends IService<SshHostWithPassword> {

    String getPasswordByHostId(Integer hostId);
}
