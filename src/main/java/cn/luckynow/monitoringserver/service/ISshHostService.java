package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.SshHost;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-08-28
 */
public interface ISshHostService extends IService<SshHost> {

    List<SshHost> getListByUserId(Long userId);

}
