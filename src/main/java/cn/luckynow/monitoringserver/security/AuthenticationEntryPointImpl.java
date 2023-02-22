package cn.luckynow.monitoringserver.security;

import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 认证失败，如账号密码错误
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result result = new Result(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录", null);
        String json = JSONUtil.toJsonStr(result);
        WebUtils.renderString(response,json);
    }
}
