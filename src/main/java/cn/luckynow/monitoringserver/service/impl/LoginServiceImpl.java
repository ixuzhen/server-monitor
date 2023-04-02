package cn.luckynow.monitoringserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.constants.RedisConstants;
import cn.luckynow.monitoringserver.entity.LoginUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.service.LoginServcie;
import cn.luckynow.monitoringserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Override
    public Result login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        // TODO: 这里token有效期要变成永久的，目前是24消失
        String jwt = JwtUtil.createJWT(userId);

        // 存到 session 中
        //HttpSession session = request.getSession();
        //session.setAttribute(userId,loginUser);

        // 存到 application 当中
        //servletContext.setAttribute(userId,loginUser);

        // 存到 redis 中
        // 转成map
        //Map<String, Object> loginUserMap = BeanUtil.beanToMap(loginUser);
        // 转成 json
        String loginUserJson = JSONUtil.toJsonStr(loginUser);
        // 存到 redis 中
        String tokenKey = RedisConstants.LOGIN_USER_KEY + userId;
        stringRedisTemplate.opsForValue().set(tokenKey, loginUserJson );
        // 设置有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);


        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.successWithData(map);
    }


    // TODO: 退出登录要从redis中删除数据
    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //HttpSession session = request.getSession();
        //session.removeAttribute(userId);
        //servletContext.removeAttribute(userId);
        stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + userId);
        return Result.successWithMessage("退出成功");
    }
}
