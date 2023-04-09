package cn.luckynow.monitoringserver.message.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class WeChatRobot {

    public static String sendMessage(String webhook ,String title, String body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject messageJson = JSONUtil.createObj();
        messageJson.putOnce("title", title);
        messageJson.putOnce("content", body);
        JSONObject sendJson = JSONUtil.createObj();
        sendJson.putOnce("msgtype","text");
        sendJson.putOnce("text", messageJson);
        String sendStr = sendJson.toString();
        //log.error(sendStr);
        HttpEntity<String> request = new HttpEntity<>(sendStr, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(webhook, request, String.class);
        //log.error(responseEntity.toString());
        return responseEntity.toString();
    }



}
