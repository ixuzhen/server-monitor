package cn.luckynow.monitoringserver.security;


import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 权限错误

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result result = new Result(HttpStatus.FORBIDDEN.value(), "权限不足", null);
        String json = JSONUtil.toJsonStr(result);
        WebUtils.renderString(response,json);

    }
}

