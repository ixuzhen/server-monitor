package cn.luckynow.monitoringserver.websocket.impl;

import cn.luckynow.monitoringserver.constants.WebSSHConstantPool;
import cn.luckynow.monitoringserver.service.ISshHostService;
import cn.luckynow.monitoringserver.service.ISshHostWithPasswordService;
import cn.luckynow.monitoringserver.websocket.WebSSHService;
import cn.luckynow.monitoringserver.websocket.pojo.SSHConnectInfo;
import cn.luckynow.monitoringserver.websocket.pojo.WebSSHData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @Description: WebSSH业务逻辑实现
*/
@Service
public class WebSSHServiceImpl implements WebSSHService {
    //存放ssh连接信息的map
    private static Map<String, Object> sshMap = new ConcurrentHashMap<>();

    private Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);
    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Resource
    private ISshHostWithPasswordService iSshHostWithPasswordService;

    /**
     * @Description: 初始化连接
     */
    @Override
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setjSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
        String uuid = String.valueOf(session.getAttributes().get(WebSSHConstantPool.USER_UUID_KEY));
        //将这个ssh连接信息放入map中
        sshMap.put(uuid, sshConnectInfo);
    }

    /**
     * @Description: 处理客户端发送的数据
     */
    @Override
    public void recvHandle(String buffer, WebSocketSession session){
        ObjectMapper objectMapper = new ObjectMapper();
        WebSSHData webSSHData = null;
        try {
            webSSHData = objectMapper.readValue(buffer, WebSSHData.class);

        } catch (IOException e) {
            logger.error("Json转换异常");
            logger.error("异常信息:{}", e.getMessage());
            return;
        }
        String userId = String.valueOf(session.getAttributes().get(WebSSHConstantPool.USER_UUID_KEY));
        if (WebSSHConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            String password = iSshHostWithPasswordService.getPasswordByHostId(webSSHData.getId());
            webSSHData.setPassword(password);
            //启动线程异步处理
            WebSSHData finalWebSSHData = webSSHData;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectToSSH(sshConnectInfo, finalWebSSHData, session);
                    } catch (JSchException | IOException e) {
                        logger.error("webssh连接异常!");
                        logger.error("异常信息:{}", e.getMessage());
                        String message = "连接异常,请检查IP，端口号，用户名，密码是否正确。异常信息" + e.getMessage();
                        sendMessage(sshConnectInfo.getWebSocketSession(), message.getBytes());
                        close(session);
                    }
                }
            });
        } else if (WebSSHConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
            String command = webSSHData.getCommand();
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            if (sshConnectInfo != null) {
                try {
                    transToSSH(sshConnectInfo.getChannel(), command);
                } catch (IOException e) {
                    logger.error("webssh连接异常!!");
                    logger.error("异常信息:{}", e.getMessage());
                    sendMessage(sshConnectInfo.getWebSocketSession(), "连接关闭".getBytes());
                    close(session);

                }
            }
        } else {
            logger.error("不支持的操作");
            close(session);
        }
    }

    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer)  {
//        logger.info("发送数据:{}", Arrays.toString(buffer));
        try {
            session.sendMessage(new TextMessage(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(WebSSHConstantPool.USER_UUID_KEY));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) sshConnectInfo.getChannel().disconnect();
            //map中移除
            sshMap.remove(userId);
        }
    }

    /**
     * @Description: 使用jsch连接终端
     */
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session = null;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        session.setConfig(config);
        //设置密码
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(30000);

        //开启shell通道
        Channel channel = session.openChannel("shell");

        //通道连接 超时时间3s
        channel.connect(3000);

        //设置channel
        sshConnectInfo.setChannel(channel);

        //转发消息
        transToSSH(channel, "\r");

        //读取终端返回的信息流
        InputStream inputStream = channel.getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }

        } catch (Exception e) {
            logger.error("webssh连接异常");
            logger.error("异常信息:{}", e.getMessage());
            close(webSocketSession);
        }
        finally {
            //断开连接后关闭会话
            session.disconnect();
            channel.disconnect();
            logger.info("webssh连接断开-----------------------");
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    /**
     * @Description: 将消息转发到终端
     */
    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
