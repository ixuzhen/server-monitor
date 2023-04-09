package cn.luckynow.monitoringserver.schedule;

import cn.luckynow.monitoringserver.entity.DiskUsage;
import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.entity.WarningLink;
import cn.luckynow.monitoringserver.service.IDiskUsageService;
import cn.luckynow.monitoringserver.service.IHostsService;
import cn.luckynow.monitoringserver.service.IWarningLinkService;
import cn.luckynow.monitoringserver.util.CommonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private IDiskUsageService iDiskUsageService;

    @Autowired
    private IHostsService iHostsService;

    @Autowired
    private IWarningLinkService iWarningLinkService;

    /**
     * 定时检查磁盘剩余空间
     * 每天10点和20点执行
     */
    //@Scheduled(fixedRate = 3000)
    @Scheduled(cron = "0 0 10,20 * * *")
    //@Scheduled(cron = "0 13 17 * * *")
    public void scheduledTask() {
        //
        List<DiskUsage> noSpaceDisks = new LinkedList<>();
        List<Host> hosts = iHostsService.list();
        for (Host host : hosts) {
            String ip = host.getIp();
            QueryWrapper<DiskUsage> QueryWrapper = new QueryWrapper<>();
            QueryWrapper.eq("ip", ip)
                    .apply("date_disk = (select Max(date_disk) from disk_usage where ip = '" + ip + "')");
            List<DiskUsage> diskUsageList = iDiskUsageService.list(QueryWrapper);
            for (DiskUsage diskUsage : diskUsageList) {
                if (diskUsage.getPercent() > 90) {
                    noSpaceDisks.add(diskUsage);
                }
            }
        }
        if (noSpaceDisks.size() > 0) {
            // TODO: 发送提醒消息
            StringBuilder sb = new StringBuilder();
            sb.append("磁盘空间不足: \n");
            for (DiskUsage diskUsage : noSpaceDisks) {
                sb.append(diskUsage.getIp() + " " + diskUsage.getMountpoint() + " " + diskUsage.getPercent() + "% ");
                String total = CommonUtils.byteToKbMbGbTb(diskUsage.getTotal());
                String used = CommonUtils.byteToKbMbGbTb(diskUsage.getUsed());
                String free = CommonUtils.byteToKbMbGbTb(diskUsage.getFree());
                sb.append("已用: " + used + " / " + total + " 剩余: " + free + "\n");
            }
            List<WarningLink> list = iWarningLinkService.list();
            RestTemplate restTemplate = new RestTemplate();
            for (WarningLink warningLink : list) {
                String link = warningLink.getLink();
                String url = link + "?title=磁盘空间报警&content=" + sb.toString();
                restTemplate.getForObject(url, String.class);
            }
        }



    }

}
