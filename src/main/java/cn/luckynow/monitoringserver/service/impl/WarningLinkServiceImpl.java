package cn.luckynow.monitoringserver.service.impl;

import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.entity.WarningLink;
import cn.luckynow.monitoringserver.mapper.WarningLinkMapper;
import cn.luckynow.monitoringserver.service.IWarningLinkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xz
 * @since 2023-04-09
 */
@Service
public class WarningLinkServiceImpl extends ServiceImpl<WarningLinkMapper, WarningLink> implements IWarningLinkService {

    @Override
    public boolean saveUrl(WarningLink warningLink) {
        // 必须确保数据库中没有才行
        QueryWrapper<WarningLink> wrapper = new QueryWrapper<>();
        wrapper.eq("link", warningLink.getLink());
        int count = count(wrapper);
        if (count != 0)
            throw new RuntimeException("已有链接，不能添加");
        return save(warningLink);
    }
}
