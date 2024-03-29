package cn.luckynow.monitoringserver.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @Description: WebSSH的业务逻辑
 */
public interface WebSSHService {
    /**
     * @Description: 初始化ssh连接
     */
    public void initConnection(WebSocketSession session);

    /**
     * @Description: 处理客户段发的数据
     */
    public void recvHandle(String buffer, WebSocketSession session);

    /**
     * @Description: 数据写回前端 for websocket
     */
    public void sendMessage(WebSocketSession session, byte[] buffer);

    /**
     * @Description: 关闭连接
     */
    public void close(WebSocketSession session);
}
