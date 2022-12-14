package cn.luckynow.monitoringserver.controller;

import cn.luckynow.monitoringserver.entity.*;
import cn.luckynow.monitoringserver.service.IDiskUsageService;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import cn.luckynow.monitoringserver.service.IGpuProcService;
import cn.luckynow.monitoringserver.service.IHostsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/hosts")
    public Result sendHostsInfo(){
        List<Host> list = iHostsService.list();
        log.info("发送了主机列表信息");
        return Result.successWithData(list);
    }

    /**
     * 发送最新 GPU 信息
     * @param ip
     * @return
     */
    @GetMapping("/gpu")
    public Result sendGpuInfo(@RequestParam String ip){
        log.info(ip);
        QueryWrapper<GpuInfo> gpuInfoQueryWrapper = new QueryWrapper<>();
        gpuInfoQueryWrapper.eq("ip", ip)
                .apply("date_gpu = (select Max(date_gpu) from gpu_info where ip = '" + ip + "')");
        List<GpuInfo> gpuList = iGpuInfoService.list(gpuInfoQueryWrapper);
        log.info("发送了 GPU 信息");
        return Result.successWithData(gpuList);
    }

    /**
     * 发送最新 GPU 进程信息
     * @param ip
     * @return
     */
    @GetMapping("/gpu/proc")
    public Result sendGpuProcInfo(@RequestParam String ip){

        QueryWrapper<GpuProc> gpuInfoQueryWrapper = new QueryWrapper<>();
        gpuInfoQueryWrapper.eq("ip", ip)
                .apply("date_gpu_proc = (select Max(date_gpu_proc) from gpu_proc where ip = '" + ip + "')");
        List<GpuProc> gpuList = iGpuProcService.list(gpuInfoQueryWrapper);
        log.info("发送了 GPU 进程信息");
        return Result.successWithData(gpuList);
    }

    @GetMapping("/disk/usage")
    public Result sendDiskUsageInfo(@RequestParam String ip){
        QueryWrapper<DiskUsage> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("ip", ip)
                .apply("date_disk = (select Max(date_disk) from disk_usage where ip = '" + ip + "')");
        List<DiskUsage> diskUsageList = iDiskUsageService.list(QueryWrapper);
        log.info("发送了硬盘用量信息");
        return Result.successWithData(diskUsageList);
    }

    @GetMapping(value = {"/unusedGPU","usedGPU"})
    public Result sendUnusedGPUInfo(){
        // 得到所有许可的主机
        List<Host> hosts = iHostsService.list();

        List<GpuInfo> allGpuInfo = new ArrayList<>();

        // 便利所有许可的主机中未使用的显卡
        for (Host host : hosts) {
            String ip = host.getIp();
            // 得到该 IP 主机的最新显卡信息
            QueryWrapper<GpuInfo> gpuInfoQueryWrapper = new QueryWrapper<>();
            gpuInfoQueryWrapper.eq("ip", ip)
                    .apply("date_gpu = (select Max(date_gpu) from gpu_info where ip = '" + ip + "')");
            List<GpuInfo> gpuList = iGpuInfoService.list(gpuInfoQueryWrapper);
            if (gpuList == null || gpuList.size()==0)
                continue;
            // 得到该 IP 主机的最新的显卡进程信息
            QueryWrapper<GpuProc> gpuProcInfoQueryWrapper = new QueryWrapper<>();
            gpuProcInfoQueryWrapper.eq("ip", ip)
                    .eq("date_gpu_proc", gpuList.get(0).getDateGpu());
            List<GpuProc> gpuProcList = iGpuProcService.list(gpuProcInfoQueryWrapper);
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
        System.out.println("发送了是否使用的GPU信息");
        return Result.successWithData(allGpuInfo);
    }


}
