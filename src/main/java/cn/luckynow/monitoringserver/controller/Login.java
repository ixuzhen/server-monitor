package cn.luckynow.monitoringserver.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Login {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
