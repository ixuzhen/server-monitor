package cn.luckynow.monitoringserver.websocket;

import cn.luckynow.monitoringserver.constants.WebSSHConstantPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component
@Slf4j
public class WebSSHWebSocketHandler implements WebSocketHandler {

    @Autowired
    private WebSSHService webSSHService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 当 WebSocket 连接建立时调用
        log.info("用户:{},连接WebSSH", session.getAttributes().get(WebSSHConstantPool.USER_UUID_KEY));
        //调用初始化连接
        webSSHService.initConnection(session);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {
//            log.info("用户:{},发送命令:{}", webSocketSession.getAttributes().get(WebSSHConstantPool.USER_UUID_KEY), webSocketMessage.toString());
            //调用service接收消息
            webSSHService.recvHandle(((TextMessage) webSocketMessage).getPayload(), webSocketSession);
        } else if (webSocketMessage instanceof BinaryMessage) {

        } else if (webSocketMessage instanceof PongMessage) {

        } else {
            log.info("Unexpected WebSocket message type: " + webSocketMessage);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("断开连接");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
