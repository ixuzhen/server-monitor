package cn.luckynow.monitoringserver.service;

import cn.luckynow.monitoringserver.entity.Port;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xz
 * @since 2023-03-02
 */
public interface IPortService extends IService<Port> {


    Port getNewestOnePortByIp(String ip);
    Timestamp getNewestDatePortByIp(String ip);
    List<Port> getPortByIpAndDate(String ip, Timestamp date);
    List<Port> getNewestPortListByIp(String ip);
}
