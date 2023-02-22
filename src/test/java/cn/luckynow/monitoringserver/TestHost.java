package cn.luckynow.monitoringserver;

import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.service.IHostsService;
import cn.luckynow.monitoringserver.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestHost {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IHostsService iHostsService;

    @Test
    public void testQuery(){
        QueryWrapper<Host> hostQueryWrapper = new QueryWrapper<>();
        String queryStr = "125.216.243.2091";
        hostQueryWrapper.eq("ip", queryStr);
        Host one = iHostsService.getOne(hostQueryWrapper);
        System.out.println(one);

    }

    @Test
    public void user(){
        int count = iUserService.count();
        System.out.println(count);

    }


}
