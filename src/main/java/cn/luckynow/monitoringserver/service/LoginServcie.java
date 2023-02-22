package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;

public interface LoginServcie {

    Result login(User user);

    Result logout();

}
