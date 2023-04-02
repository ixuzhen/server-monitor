package cn.luckynow.monitoringserver.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.constants.RedisConstants;
import cn.luckynow.monitoringserver.entity.LoginUser;
import cn.luckynow.monitoringserver.util.JwtUtil;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationTokenFilter  extends OncePerRequestFilter {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        request.setAttribute("userid", userid);
        //从session中获取用户信息
        //HttpSession session = request.getSession();
        //LoginUser loginUser = (LoginUser) session.getAttribute(userid);
        // 从appliction 中获取用户信息
        //LoginUser loginUser = (LoginUser) servletContext.getAttribute(userid);

        // 从 redis 中获取用户信息
        String key = RedisConstants.LOGIN_USER_KEY + userid;
        //Map<Object, Object> LoginUserMap = stringRedisTemplate.opsForHash().entries(key);
        String loginUserStr = stringRedisTemplate.opsForValue().get(key);
        JSONObject loginUserJson = JSONUtil.parseObj(loginUserStr);
        LoginUser loginUser = loginUserJson.toBean(LoginUser.class);
        if(Objects.isNull(loginUser)){
        //if(LoginUserMap.isEmpty()){
            throw new RuntimeException("用户未登录");
        }
        //LoginUser loginUser = BeanUtil.fillBeanWithMap(LoginUserMap, new LoginUser(), false);
        // 刷新token有效期
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
