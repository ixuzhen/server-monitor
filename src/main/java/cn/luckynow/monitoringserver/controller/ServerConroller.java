package cn.luckynow.monitoringserver.controller;

import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.entity.GpuProc;
import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import cn.luckynow.monitoringserver.service.IGpuProcService;
import cn.luckynow.monitoringserver.service.IHostsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/hosts")
    public Result sendHostsInfo(){
        log.info("send hosts info");
        List<Host> list = iHostsService.list();

        return Result.successWithData(list);
    }

    @GetMapping("/gpu")
    public Result sendGpuInfo(@RequestParam String ip){
        log.info("send gpu info");
        log.info(ip);
        QueryWrapper<GpuInfo> gpuInfoQueryWrapper = new QueryWrapper<>();
        gpuInfoQueryWrapper.eq("ip", ip)
                .apply("date_gpu = (select Max(date_gpu) from gpu_info where ip = '" + ip + "')");
        List<GpuInfo> gpuList = iGpuInfoService.list(gpuInfoQueryWrapper);
        log.info(gpuList.toString());
        return Result.successWithData(gpuList);
    }

    @GetMapping("/gpu/proc")
    public Result sendGpuProcInfo(@RequestParam String ip){
        log.info("send gpu proc info");
        log.info(ip);
        QueryWrapper<GpuProc> gpuInfoQueryWrapper = new QueryWrapper<>();
        gpuInfoQueryWrapper.eq("ip", ip)
                .apply("date_gpu_proc = (select Max(date_gpu_proc) from gpu_proc where ip = '" + ip + "')");
        List<GpuProc> gpuList = iGpuProcService.list(gpuInfoQueryWrapper);
        log.info(gpuList.toString());
        return Result.successWithData(gpuList);
    }


}
