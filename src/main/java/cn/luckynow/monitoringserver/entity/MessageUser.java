package cn.luckynow.monitoringserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author xz
 * @since 2023-02-24
 */
@TableName("message_user")
@ApiModel(value = "MessageUser对象", description = "")
public class MessageUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("推送使用的用户名,唯一")
    private String username;

    @ApiModelProperty("推送使用的token")
    private String token;

    @ApiModelProperty("推送方式")
    private String channel;

    @ApiModelProperty("要推送的邮箱")
    private String email;

    @ApiModelProperty("钉钉群机器人的webhook的url")
    private String dingWebhookUrl;

    @ApiModelProperty("钉钉群机器人的webhook的secret")
    private String dingWebhookSecret;

    @ApiModelProperty("飞书群机器人的webhook的url")
    private String feishuWebhookUrl;

    @ApiModelProperty("飞书群机器人的webhook的secret")
    private String feishuWebhookSecret;

    @ApiModelProperty("企业微信群机器人的webhook的url")
    private String wechatWebhookUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getDingWebhookUrl() {
        return dingWebhookUrl;
    }

    public void setDingWebhookUrl(String dingWebhookUrl) {
        this.dingWebhookUrl = dingWebhookUrl;
    }
    public String getDingWebhookSecret() {
        return dingWebhookSecret;
    }

    public void setDingWebhookSecret(String dingWebhookSecret) {
        this.dingWebhookSecret = dingWebhookSecret;
    }
    public String getFeishuWebhookUrl() {
        return feishuWebhookUrl;
    }

    public void setFeishuWebhookUrl(String feishuWebhookUrl) {
        this.feishuWebhookUrl = feishuWebhookUrl;
    }
    public String getFeishuWebhookSecret() {
        return feishuWebhookSecret;
    }

    public void setFeishuWebhookSecret(String feishuWebhookSecret) {
        this.feishuWebhookSecret = feishuWebhookSecret;
    }
    public String getWechatWebhookUrl() {
        return wechatWebhookUrl;
    }

    public void setWechatWebhookUrl(String wechatWebhookUrl) {
        this.wechatWebhookUrl = wechatWebhookUrl;
    }

    @Override
    public String toString() {
        return "MessageUser{" +
            "id=" + id +
            ", username=" + username +
            ", token=" + token +
            ", channel=" + channel +
            ", email=" + email +
            ", dingWebhookUrl=" + dingWebhookUrl +
            ", dingWebhookSecret=" + dingWebhookSecret +
            ", feishuWebhookUrl=" + feishuWebhookUrl +
            ", feishuWebhookSecret=" + feishuWebhookSecret +
            ", wechatWebhookUrl=" + wechatWebhookUrl +
        "}";
    }
}
