package cn.luckynow.monitoringserver.common;

import lombok.Data;

/**
 * @description:
 * @author：xz
 * @date: 2023/5/25
 */
public class Constants {

    public enum MessageType {
        EMAIL(1, "邮件"),
        FEISHU(2, "飞书"),
        DINGDING(3, "钉钉"),
        WECHAT(4, "企业微信");

        private Integer code;
        private String info;

        MessageType(Integer code, String info) {
            this.code = code;
            this.info = info;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
