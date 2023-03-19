package cn.luckynow.monitoringserver.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value = "/terminal", configurator = WebSocketGetHttpSessionConfigurator.class)
@Slf4j
public class TerminalEndPoint {
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        log.error("onOpen");
    }

    //收到消息
    @OnMessage
    public void onMessage(String message, Session session) {
        log.error("onMessage");
    }


    //关闭
    @OnClose
    public void onClose(Session session) {
        log.error("onClose");
    }
}
