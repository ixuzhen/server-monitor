package cn.luckynow.monitoringserver.service.impl;

import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.constants.RedisConstants;
import cn.luckynow.monitoringserver.entity.LoginUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.mapper.UserMapper;
import cn.luckynow.monitoringserver.service.IMessageUserService;
import cn.luckynow.monitoringserver.service.IUserService;
import cn.luckynow.monitoringserver.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xz
 * @since 2023-02-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {




    @Override
    public boolean saveUser(User user) {
        // 必须确保数据库中没有才行
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",user.getUserName());
        int count = count(wrapper);
        if(count!=0)
            throw new RuntimeException("已有用户，不能注册");
        return save(user);
    }

    @Override
    public List<User> getUserByGithubId(Long idGithub) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id_github",idGithub);
        return list(wrapper);
    }



}
