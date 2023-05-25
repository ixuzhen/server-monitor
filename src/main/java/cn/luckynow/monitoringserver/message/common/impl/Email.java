package cn.luckynow.monitoringserver.message.common.impl;

import cn.luckynow.monitoringserver.entity.MessageUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.message.common.ISendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

//@RequiredArgsConstructor
@Slf4j
@Component
public class Email implements ISendMessage {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String account;

    public String sendEmail(String account, String address, String title, String body){
        //String address = "503776203@qq.com";
        //String title = "测试";
        //String body = "这是测试邮件";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(account);
        simpleMailMessage.setTo(address);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(body);
        try{
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            e.printStackTrace();
            log.error("发送邮件失败");
            return "发送邮件失败";
        }
        return "发送邮件成功";
    }

    @Override
    public String sendMessage(MessageUser messageUser, String titile, String content) {
        return sendEmail(account, messageUser.getEmail(), titile, content);
    }

    //public static void main(String[] args) {
    //    new Email().sendEmail("503776203@qq.com", "测试", "这是测试邮件");
    //}

    
}

