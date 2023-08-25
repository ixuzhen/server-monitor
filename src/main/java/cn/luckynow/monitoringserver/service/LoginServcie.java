package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;

public interface LoginServcie {

    Result login(User user);

    Result loginGithub(String code);

    Result logout();

    Result register(User user);

    Result loginGitee(String code);

}
