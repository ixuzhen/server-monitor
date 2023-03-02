package cn.luckynow.monitoringserver.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.MessageUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.service.IMessageUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
@Slf4j
public class AlterMessageInfoController {

    @Autowired
    private IMessageUserService iMessageUserService;

    @GetMapping("/info")
    public Result getMessageUserInfo( HttpServletRequest request){
        String userid = (String) request.getAttribute("userid");
        MessageUser user = iMessageUserService.getById(Long.parseLong(userid));
        return Result.successWithData(user);
    }



    @PostMapping("/username")
    public Result setUsername(@RequestBody String data){
        JSONObject dataJson = JSONUtil.parseObj(data);
        String username = (String) dataJson.get("username");
        boolean success = iMessageUserService.alterUsername(username);
        if( success )
            return Result.successWithoutData();
        else
            return Result.failed("用户名已存在");
    }

    @PostMapping("/email")
    public Result setEmail(@RequestBody String data){
        // TODO：对邮箱的身份做校验，发送验证码的方式
        JSONObject dataJson = JSONUtil.parseObj(data);
        String email = (String) dataJson.get("email");
        boolean success = iMessageUserService.alterEmailAddress(email);
        if( success )
            return Result.successWithoutData();
        else
            return Result.failed("设置邮箱失败");
    }

    @PostMapping("/dingding")
    public Result setDingDingWebhook(@RequestBody String data){
        JSONObject dataJson = JSONUtil.parseObj(data);
        String url = (String) dataJson.get("url");
        String secret = (String) dataJson.get("secret");
        boolean success = iMessageUserService.alterDingDingWebhook(url, secret);
        if( success )
            return Result.successWithoutData();
        else
            return Result.failed("设置钉钉 webhook 失败");
    }

    @PostMapping("/feishu")
    public Result setFeishuWebhook(@RequestBody String data){
        JSONObject dataJson = JSONUtil.parseObj(data);
        String url = (String) dataJson.get("url");
        String secret = (String) dataJson.get("secret");
        boolean success = iMessageUserService.alterFeiShuWebhook(url, secret);
        if( success )
            return Result.successWithoutData();
        else
            return Result.failed("设置飞书 webhook 失败");
    }

    @PostMapping("/wechat")
    public Result setWechatWebhook(@RequestBody String data){
        JSONObject dataJson = JSONUtil.parseObj(data);
        String url = (String) dataJson.get("url");
        boolean success = iMessageUserService.alterWeChatWebhook(url);
        if( success )
            return Result.successWithoutData();
        else
            return Result.failed("设置企业微信 webhook 失败");
    }

    @PostMapping("/pushway")
    public Result setPushWay(@RequestBody String data){
        JSONObject dataJson = JSONUtil.parseObj(data);
        //log.error( dataJson.get("pushway").toString());
        String pushWay = (String) dataJson.get("pushway");
        boolean success = iMessageUserService.alterPushWay(pushWay);
        if( success )
            return Result.successWithoutData();
        else
            return Result.failed("设置默认推送失败");
    }

}