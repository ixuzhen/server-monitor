package cn.luckynow.monitoringserver.message.common;


import cn.luckynow.monitoringserver.common.Constants;
import cn.luckynow.monitoringserver.message.common.impl.DingDingRobot;
import cn.luckynow.monitoringserver.message.common.impl.Email;
import cn.luckynow.monitoringserver.message.common.impl.FeiShuRobot;
import cn.luckynow.monitoringserver.message.common.impl.WeChatRobot;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：xz
 * @date: 2023/5/25
 */
@Service
public class MessageFactory {
    private Map<Integer, ISendMessage> messageMap = new HashMap<>();

    @Resource
    private Email email;

    @Resource
    private FeiShuRobot feiShuRobot;

    @Resource
    private DingDingRobot dingDingRobot;

    @Resource
    private WeChatRobot weChatRobot;

    @PostConstruct
    public void init() {
        messageMap.put(Constants.MessageType.EMAIL.getCode(), email);
        messageMap.put(Constants.MessageType.FEISHU.getCode(), feiShuRobot);
        messageMap.put(Constants.MessageType.DINGDING.getCode(), dingDingRobot);
        messageMap.put(Constants.MessageType.WECHAT.getCode(), weChatRobot);
    }

    public ISendMessage getMessageMethod(Integer code) {
        return messageMap.get(code);
    }

}
