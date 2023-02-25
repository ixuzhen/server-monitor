package cn.luckynow.monitoringserver.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.LoginUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.service.IMessageUserService;
import cn.luckynow.monitoringserver.service.IUserService;
import cn.luckynow.monitoringserver.service.LoginServcie;
import cn.luckynow.monitoringserver.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;


@RestController
@Slf4j
@CrossOrigin
public class Login {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private LoginServcie loginServcie;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IMessageUserService iMessageUserService;

    @PostMapping("/login")
    public Result login(@RequestBody String userData){
        JSONObject userDataJson = JSONUtil.parseObj(userData);
        String username = (String) userDataJson.get("username");
        String password = (String) userDataJson.get("password");
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        return loginServcie.login(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody String userData){
        JSONObject userDataJson = JSONUtil.parseObj(userData);
        String username = (String) userDataJson.get("username");
        String password = (String) userDataJson.get("password");
        password = passwordEncoder.encode(password);
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        boolean success = iUserService.saveUser(user);
        if(!success)
            return Result.failed("注册失败！！");
        boolean success_message = iMessageUserService.saveMessageUser(user);
        if(!success_message)
            log.error("注册消息推送服务失败");
        String userId = user.getId().toString();
        HttpSession session = request.getSession();
        LoginUser loginUser = new LoginUser(user);
        session.setAttribute(userId,loginUser);
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.successWithData(map);
    }


}
