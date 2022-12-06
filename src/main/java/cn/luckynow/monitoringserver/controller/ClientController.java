package cn.luckynow.monitoringserver.controller;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.entity.GpuProc;
import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import cn.luckynow.monitoringserver.service.IGpuProcService;
import cn.luckynow.monitoringserver.service.IHostsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @Autowired
    private IGpuInfoService iGpuInfoService;

    @Autowired
    private IHostsService iHostsService;

    @Autowired
    private IGpuProcService iGpuProcService;


    @PostMapping("/host/info")
    public String receiveHostInfo(@RequestBody String hostInfo) {
        JSONObject jsonObject = JSONUtil.parseObj(hostInfo);
        Host host = jsonObject.toBean(Host.class);
        QueryWrapper<Host> hostQueryWrapper = new QueryWrapper<>();
        hostQueryWrapper.eq("ip", host.getIp());
        Host queryHost = iHostsService.getOne(hostQueryWrapper);
        if (queryHost == null) {
            iHostsService.save(host);
            log.info("插入一个主机：" + host.toString());
        }
        return "receive host info success";
    }


    /**
     * 接受 gpu 信息
     *
     * @return
     */
    @PostMapping("/gpu/info")
    public String receiveGPUInfo(@RequestBody String gpu_info) {
        JSONObject jsonObject = JSONUtil.parseObj(gpu_info);
        System.out.println(jsonObject.get("ip"));
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        JSONArray allGPUInfo = jsonObject.get("gpuInfo", JSONArray.class);
        int gpu_count = allGPUInfo.size();
        for (int i = 0; i < gpu_count; i++) {
            JSONObject oneGPUInfo = allGPUInfo.getJSONObject(i);
            oneGPUInfo.putOnce("ip", ip);
            oneGPUInfo.putOnce("id_host", 12);
            oneGPUInfo.putOnce("date_gpu", date);
            GpuInfo oneGPUBean = oneGPUInfo.toBean(GpuInfo.class);
            iGpuInfoService.save(oneGPUBean);
            log.info("插入一条数据：{oneGPUBean}");
        }
        //log.info("{date} {ip} 发送了 GPU 信息");
        return "receive gpu info success";
    }

    @PostMapping("/gpu/proc/info")
    public String receiveGPUProcInfo(@RequestBody String gpu_info) {
        JSONObject jsonObject = JSONUtil.parseObj(gpu_info);
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        JSONArray allGPUProcInfo = jsonObject.get("gpu_proc_info", JSONArray.class);
        for (int i = 0; i < allGPUProcInfo.size(); i++) {
            JSONObject oneGPUProcInfo = allGPUProcInfo.getJSONObject(i);
            GpuProc gpuProc = oneGPUProcInfo.toBean(GpuProc.class);
            gpuProc.setIp(ip);
            gpuProc.setDateGpuProc(date);
            iGpuProcService.save(gpuProc);
            log.info("插入一条数据：{}", gpuProc);
        }

        return "receive gpu proc info success";
    }


}
