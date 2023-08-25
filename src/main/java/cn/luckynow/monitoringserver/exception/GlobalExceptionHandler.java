package cn.luckynow.monitoringserver.exception;

import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //@Autowired
    //private HttpServletResponse response;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result customException(Exception e) {

        //log.error(e.getCause().getMessage());
        //log.error(e.getMessage());
        e.printStackTrace();
        //WebUtils.initResponse(response);
        return Result.failed(e.getMessage());
    }
}
