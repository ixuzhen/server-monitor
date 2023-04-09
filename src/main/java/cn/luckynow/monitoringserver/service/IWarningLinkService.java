package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.WarningLink;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-04-09
 */
public interface IWarningLinkService extends IService<WarningLink> {

    boolean saveUrl(WarningLink warningLink);
}
