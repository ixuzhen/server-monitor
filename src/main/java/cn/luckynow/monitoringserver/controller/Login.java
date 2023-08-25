package cn.luckynow.monitoringserver.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.service.LoginServcie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@Slf4j
@CrossOrigin
public class Login {

    @Autowired
    private LoginServcie loginServcie;

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

    @PostMapping("/login/github")
    public Result githubLogin(@RequestBody String userData){
        JSONObject userDataJson = JSONUtil.parseObj(userData);
        String code = (String) userDataJson.get("code");
        return loginServcie.loginGithub(code);
    }

    @PostMapping("/register")
    public Result register(@RequestBody String userData){
        JSONObject userDataJson = JSONUtil.parseObj(userData);
        String username = (String) userDataJson.get("username");
        String password = (String) userDataJson.get("password");
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        return loginServcie.register(user);
    }


}
