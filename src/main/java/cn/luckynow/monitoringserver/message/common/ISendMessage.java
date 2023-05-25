package cn.luckynow.monitoringserver.message.common;

import cn.luckynow.monitoringserver.entity.MessageUser;

/**
 * @description:
 * @author：xz
 * @date: 2023/5/25
 */
public interface ISendMessage {

    String sendMessage(MessageUser messageUser, String titile, String content);

}
