package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xz
 * @since 2023-02-21
 */
public interface IUserService extends IService<User> {
    public boolean saveUser(User user);
}
