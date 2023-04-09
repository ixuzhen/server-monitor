package cn.luckynow.monitoringserver.message.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Slf4j
public class DingDingRobot {

    public static String sendMessage(String webhook, String secret ,String title, String body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject messageJson = JSONUtil.createObj();
        JSONObject sendJson = JSONUtil.createObj();
        messageJson.putOnce("title", title);
        messageJson.putOnce("content", body);
        sendJson.putOnce("msgtype","text");
        sendJson.putOnce("text", messageJson);
        // 发送 markdown的格式
        // messageJson.putOnce("title", title);
        // messageJson.putOnce("text", body);
        // sendJson.putOnce("msgtype","markdown");
        // sendJson.putOnce("markdown", messageJson);
        long timestamp = System.currentTimeMillis();
        String newSecret = GenSign(secret, timestamp);
        String sendStr = sendJson.toString();
        log.error(sendStr);
        String url = webhook + "&timestamp=" + String.valueOf(timestamp) + "&sign=" + newSecret;
        HttpEntity<String> request = new HttpEntity<>(sendStr, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        //log.error(responseEntity.toString());
        return responseEntity.toString();
    }

    private static String GenSign(String secret,long timestamp) {

        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        //使用HmacSHA256算法计算签名
        byte[] signData = null;
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        }catch (InvalidKeyException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        //String sign = new String(Base64.encodeBase64(signData));
        String sign = null;
        try{
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return sign;
    }




}
