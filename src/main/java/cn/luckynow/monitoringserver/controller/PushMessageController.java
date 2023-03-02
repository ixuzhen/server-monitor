package cn.luckynow.monitoringserver.controller;


import cn.luckynow.monitoringserver.entity.MessageUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.message.common.*;
import cn.luckynow.monitoringserver.service.IMessageUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push")
@Slf4j
public class PushMessageController {

    @Autowired
    private IMessageUserService iMessageUserService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String account;

    @GetMapping("/{username}")
    public Result pushMessage(@PathVariable("username") String username, @RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "content", required = false) String content,
                              @RequestParam(value = "channel", required = false) String channel,
                              @RequestParam(value = "token", required = false) String token){

        // TODO：url中有特殊字符会报错，如http://localhost:8080/push/xz?title={标题}&content={内容}，{}属于特殊符号，会报错

        // 根据用户名获取消息用户
        MessageUser messageUser = iMessageUserService.getMessageUserByName(username);
        if(messageUser==null)
            return Result.failed("没有此用户名");
        log.error(messageUser.toString());
        log.error(title);
        log.error(content);
        // TODO：检测 token


        // 选择 channel，即推送方式
        int pushWay = Integer.parseInt(messageUser.getChannel());
        if(channel!=null && PushWay.map.containsKey(channel)){
            pushWay = PushWay.map.get(channel);
        }
        // TODO: 对title和content做校验
        // 处理title
        if(title == null)
            title = "";
        // 处理content
        if(content == null)
            content = "";
        // 发送消息
        String result = "";
        // TODO:对发送的邮箱做校验
        if(pushWay == PushWay.EMAIL){
            result = new Email(javaMailSender).sendEmail(account, messageUser.getEmail(), title, content);
        }else if( pushWay == PushWay.FEISHU){
            result = FeiShuRobot.sendMessage(messageUser.getFeishuWebhookUrl(),messageUser.getFeishuWebhookSecret(), title,content);
        }else if( pushWay == PushWay.DINGDING){
            result = DingDingRobot.sendMessage(messageUser.getDingWebhookUrl(), messageUser.getDingWebhookSecret(), title, content);
        }else if(pushWay == PushWay.WECHAT){
            result = WeChatRobot.sendMessage(messageUser.getWechatWebhookUrl(), title, content);
        }else{
            Result.failed("发送方式错误");
        }

        return Result.successWithData(result);
    }


}
