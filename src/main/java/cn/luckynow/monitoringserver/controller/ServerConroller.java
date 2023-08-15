package cn.luckynow.monitoringserver.controller;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.constants.RedisConstants;
import cn.luckynow.monitoringserver.entity.*;
import cn.luckynow.monitoringserver.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/server")
@Slf4j

@CrossOrigin
public class ServerConroller {

    @Autowired
    private IHostsService iHostsService;

    @Autowired
    private IGpuInfoService iGpuInfoService;

    @Autowired
    private IGpuProcService iGpuProcService;

    @Autowired
    private IDiskUsageService iDiskUsageService;

    @Autowired
    private IMemoryService iMemoryService;

    @Autowired
    private IPortService iPortService;

    @Autowired
    private IDockerInfoService iDockerInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/hosts")
    public Result sendHostsInfo(){
        List<Host> list = iHostsService.list();
        log.info("发送了主机列表信息");
        // 判断机器是否在线
        for (Host host : list) {
            String ip = host.getIp();
            String key = RedisConstants.HEART_BEAT_KEY + ip;
            if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))){
                host.setIsOnline(true);
            }else {
                host.setIsOnline(false);
            }
        }
        return Result.successWithData(list);
    }

    @GetMapping("/gpuhosts")
    public Result sendGpuHostsInfo(){
        List<Host> list = iHostsService.list();
        log.info("发送了主机列表信息");
        // 判断机器是否在线
        for (Host host : list) {
            String ip = host.getIp();
            String key = RedisConstants.HEART_BEAT_KEY + ip;
            if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))){
                host.setIsOnline(true);
            }else {
                host.setIsOnline(false);
            }
            int gpuCount = iGpuInfoService.getGPUCount(ip);
            host.setGpuCount(gpuCount);
        }
        return Result.successWithData(list);
    }

    @GetMapping("/dockerhosts")
    public Result sendDockerHostsInfo(){
        List<Host> list = iHostsService.list();
        log.info("发送了主机列表信息");
        // 判断机器是否在线
        for (Host host : list) {
            String ip = host.getIp();
            String key = RedisConstants.HEART_BEAT_KEY + ip;
            if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))){
                host.setIsOnline(true);
            }else {
                host.setIsOnline(false);
            }

            int dockerCount = iDockerInfoService.getDockerCount(ip);
            host.setDockerCount(dockerCount);
        }
        return Result.successWithData(list);
    }



    /**
     * 发送最新 GPU 信息
     * @param ip
     * @return
     */
    @GetMapping("/gpu")
    public Result sendGpuInfo(@RequestParam String ip){
        //log.info(ip);
        //QueryWrapper<GpuInfo> gpuInfoQueryWrapper = new QueryWrapper<>();
        //gpuInfoQueryWrapper.eq("ip", ip)
        //        .apply("date_gpu = (select Max(date_gpu) from gpu_info where ip = '" + ip + "')");
        //List<GpuInfo> gpuList = iGpuInfoService.list(gpuInfoQueryWrapper);
        List<GpuInfo> gpuList = iGpuInfoService.getNewestGpuInfoListByIp(ip);
        log.info("发送了 GPU 信息.");
        return Result.successWithData(gpuList);
    }

    /**
     * 发送最新 GPU 进程信息
     * @param ip
     * @return
     */
    @GetMapping("/gpu/proc")
    public Result sendGpuProcInfo(@RequestParam String ip){

        List<GpuProc> gpuList = iGpuProcService.getNewestGpuProcListByIp(ip);
        log.info("发送了 GPU 进程信息。");
        return Result.successWithData(gpuList);
    }


    /**
     *
     *
     这么写不能使用（IP，date_disk）的索引
     EXPLAIN
     SELECT MAX(date_disk) FROM disk_usage WHERE ip = '125.216.243.209'
     要下面这么写
     EXPLAIN
     SELECT * FROM disk_usage
     WHERE ip = '125.216.243.209'
     ORDER BY date_disk DESC
     LIMIT 1;
     * @param ip
     * @return
     */
    @GetMapping("/disk/usage")
    public Result sendDiskUsageInfo(@RequestParam String ip){
        //QueryWrapper<DiskUsage> QueryWrapper = new QueryWrapper<>();
        //QueryWrapper.eq("ip", ip)
        //        .apply("date_disk = (select Max(date_disk) from disk_usage where ip = '" + ip + "')");
        //List<DiskUsage> diskUsageList = iDiskUsageService.list(QueryWrapper);
        List<DiskUsage> newestDiskUsageListByIp = iDiskUsageService.getNewestDiskUsageListByIp(ip);
        log.info("发送了硬盘用量信息");
        return Result.successWithData(newestDiskUsageListByIp);
    }

    // 获取普通信息
    @GetMapping("/common")
    public Result sendCommonInfo(@RequestParam String ip){
        JSONObject result = JSONUtil.createObj();
        // 获取磁盘信息
        List<DiskUsage> diskUsageList = iDiskUsageService.getNewestDiskUsageListByIp(ip);
        if(diskUsageList!=null){
            result.putOnce("diskUsageList", diskUsageList);
            log.info("发送了硬盘用量信息。");
        }
        // 获取内存信息
        Memory memory = iMemoryService.getNewestMemoryByIp(ip);
        if(memory!=null){
            result.putOnce("memoryInfo", memory);
            log.info("发送了内存用量信息。");
        }
        // 获取端口信息
        List<Port> portList = iPortService.getNewestPortListByIp(ip);
        if(portList!=null){
            result.putOnce("portLists", portList);
            log.info("发送了端口信息。");
        }
        return Result.successWithData(result);
    }

    // 获取普通信息
    @GetMapping("/docker")
    public Result sendDockerInfoByIp(@RequestParam String ip){
        JSONObject result = JSONUtil.createObj();

        // 获取内存信息
        Memory memory = iMemoryService.getNewestMemoryByIp(ip);
        if(memory!=null){
            result.putOnce("memoryInfo", memory);
            log.info("发送了内存用量信息。");
        }
        List<DockerInfo> dockerInfo = iDockerInfoService.getNewestDockerInfoListByIp(ip);
        if(dockerInfo!=null){
            result.putOnce("dockerInfo", dockerInfo);
            log.info("发送了docker信息。");
        }
        return Result.successWithData(result);
    }


    /**
     * 发送未使用的 GPU 信息, 主机必须在线
     * @return
     */
    @GetMapping(value = {"/unusedGPU","usedGPU"})
    public Result sendUnusedGPUInfo(){
        // 得到所有许可的主机
        List<Host> hosts = iHostsService.list();
        // 过滤掉不在线的主机
        hosts = hosts.stream().filter(host -> {
            String ip = host.getIp();
            String key = RedisConstants.HEART_BEAT_KEY + ip;
            if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))){
                return true;
            }else {
                return false;
            }
        }).collect(Collectors.toList());
        List<GpuInfo> allGpuInfo = new ArrayList<>();

        // 便利所有许可的主机中未使用的显卡
        for (Host host : hosts) {
            String ip = host.getIp();
            // 得到该 IP 主机的最新显卡信息
            List<GpuInfo> gpuList = iGpuInfoService.getNewestGpuInfoListByIp(ip);
            if (gpuList == null || gpuList.size()==0){
                continue;
            }
            // 得到该 IP 主机的最新的显卡进程信息
            List<GpuProc> gpuProcList = iGpuProcService.getGpuProcByIpAndDate(ip, gpuList.get(0).getDateGpu());
            // 如果 GPU 进程类型位 C 或者 C+G 就表示在使用当中
            // http://developer.download.nvidia.com/compute/DCGM/docs/nvidia-smi-367.38.pdf
            for ( GpuProc gpuProc : gpuProcList ) {
                String typeProc = gpuProc.getTypeProc();
                Integer indexGpu = gpuProc.getIndexGpu();
                if ("C".equals(typeProc) || "C+G".equals(typeProc)) {
                    for(GpuInfo gpuInfo : gpuList){
                        if(indexGpu.equals(gpuInfo.getIndexGpu())){
                            gpuInfo.setIsUsed(true);
                        }
                    }
                }
            }
            // 将设置好的 GPU 是否使用的信息集合到一起
            allGpuInfo.addAll(gpuList);
        }
        log.info("发送了是否使用的GPU信息");
        return Result.successWithData(allGpuInfo);
    }


}
