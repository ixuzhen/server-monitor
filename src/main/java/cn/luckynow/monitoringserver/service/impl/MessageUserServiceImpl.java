package cn.luckynow.monitoringserver.service.impl;


import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.entity.MessageUser;
import cn.luckynow.monitoringserver.mapper.MessageUserMapper;
import cn.luckynow.monitoringserver.service.IMessageUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xz
 * @since 2023-02-24
 */
@Service
@Slf4j
public class MessageUserServiceImpl extends ServiceImpl<MessageUserMapper, MessageUser> implements IMessageUserService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MessageUserMapper messageUserMapper;

    @Override
    public boolean alterPushWay(String pushway) {
        String userid = (String) request.getAttribute("userid");
        UpdateWrapper<MessageUser> wrapper = new UpdateWrapper<>();
        wrapper.set("channel", pushway)
                .eq("id", Long.valueOf(userid));
        int update = messageUserMapper.update(null, wrapper);
        return update == 1;
    }

    @Override
    public boolean alterUsername(String username) {
        QueryWrapper<MessageUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        int count = count(queryWrapper);
        if(count != 0)
            return false;
        String userid = (String) request.getAttribute("userid");
        UpdateWrapper<MessageUser> wrapper = new UpdateWrapper<>();
        //log.error("userid:{}", userid);
        wrapper.set("username", username)
                .eq("id", Long.valueOf(userid));
        int update = messageUserMapper.update(null, wrapper);
        return update == 1;
    }

    @Override
    public boolean alterEmailAddress(String email) {
        String userid = (String) request.getAttribute("userid");
        UpdateWrapper<MessageUser> wrapper = new UpdateWrapper<>();
        wrapper.set("email", email)
                .eq("id", Long.valueOf(userid));
        int update = messageUserMapper.update(null, wrapper);
        return update == 1;
    }

    @Override
    public boolean alterFeiShuWebhook(String url, String secret) {
        String userid = (String) request.getAttribute("userid");
        UpdateWrapper<MessageUser> wrapper = new UpdateWrapper<>();
        wrapper.set("feishu_webhook_url", url)
                .set("feishu_webhook_secret", secret)
                .eq("id", Long.valueOf(userid));
        int update = messageUserMapper.update(null, wrapper);
        return update == 1;
    }

    @Override
    public boolean alterDingDingWebhook(String url, String secret) {
        String userid = (String) request.getAttribute("userid");
        UpdateWrapper<MessageUser> wrapper = new UpdateWrapper<>();
        wrapper.set("ding_webhook_url", url)
                .set("ding_webhook_secret", secret)
                .eq("id", Long.valueOf(userid));
        int update = messageUserMapper.update(null, wrapper);
        return update == 1;
    }

    @Override
    public boolean alterWeChatWebhook(String url) {
        String userid = (String) request.getAttribute("userid");
        UpdateWrapper<MessageUser> wrapper = new UpdateWrapper<>();
        wrapper.set("wechat_webhook_url", url)
                .eq("id", Long.valueOf(userid));
        int update = messageUserMapper.update(null, wrapper);
        return update == 1;
    }

    @Override
    public boolean saveMessageUser(User user) {
        if(user.getId() == null){
            return false;
        }
        MessageUser messageUser = new MessageUser();
        messageUser.setId(user.getId());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        messageUser.setUsername(uuid);
        return save(messageUser);
    }

    @Override
    public MessageUser getMessageUserByName(String username) {
        QueryWrapper<MessageUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<MessageUser> list = messageUserMapper.selectList(queryWrapper);
        if(list.size()==0){
            return null;
        }
        if(list.size()!=1){
            log.error("消息推送的用户名{}重复了", username);
            return null;
        }

        return list.get(0);
    }
}
