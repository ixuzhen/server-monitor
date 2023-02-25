package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.entity.MessageUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-02-24
 */
public interface IMessageUserService extends IService<MessageUser> {

    public boolean alterPushWay(String pushway);
    public boolean alterUsername(String username);
    public boolean alterEmailAddress(String email);
    public boolean alterFeiShuWebhook(String url, String secret);
    public boolean alterDingDingWebhook(String url, String secret);
    public boolean alterWeChatWebhook(String url);

    public boolean saveMessageUser(User user);

    public MessageUser getMessageUserByName(String username);
}
