package cn.luckynow.monitoringserver.constants;

public class RedisConstants {
    // 验证码存放在 redis 的名字
    public static final String LOGIN_CODE_KEY = "login:code:";
    // 验证码有效期
    public static final Long LOGIN_CODE_TTL = 2L;
    // 访问系统需要待 token
    public static final String LOGIN_USER_KEY = "login:token:";
    // token有效期，30 分钟
    public static final Long LOGIN_USER_TTL = 30L;

}
