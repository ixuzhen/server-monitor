package cn.luckynow.monitoringserver.message.common;

import com.baomidou.mybatisplus.generator.config.INameConvert;
import org.springframework.data.relational.core.sql.In;

import java.util.HashMap;
import java.util.Map;

public class PushWay {
    public static final int EMAIL = 1;
    public static final int FEISHU = 2;
    public static final int DINGDING = 3;
    public static final int WECHAT = 4;
    public static final Map<String , Integer> map = new HashMap<>();
    static {
        map.put("email", EMAIL);
        map.put("feishu", FEISHU);
        map.put("dingding", DINGDING);
        map.put("wechat", WECHAT);
    }
}
