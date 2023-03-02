package cn.luckynow.monitoringserver.controller;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.*;
import cn.luckynow.monitoringserver.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;

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

    @Autowired
    private IDiskUsageService iDiskUsageService;

    @Autowired
    private IMemoryService iMemoryService;

    @Autowired IPortService iPortService;


    @PostMapping("/data")
    public Result<Object> receiveAllData(@RequestBody String allData) {
        log.info(allData);
        JSONObject allDataJson = JSONUtil.parseObj(allData);
        Set<String> dataKeySet = allDataJson.keySet();
        String ip = allDataJson.get("ip", String.class);
        Timestamp date = new Timestamp(allDataJson.get("date", Long.class) * 1000);
        this.updateHostInfo(allDataJson);
        if (dataKeySet.contains("gpu_info"))
            this.processGpuInfo(allDataJson, "gpu_info");
        if (dataKeySet.contains("gpu_proc_info"))
            this.processGPUProcInfo(allDataJson, "gpu_proc_info");
        if (dataKeySet.contains("disk_usages"))
            this.processDiskUsageInfo(allDataJson, "disk_usages");
        if (dataKeySet.contains("memory"))
            this.processMemoryInfo(allDataJson, "memory", ip, date);
        if (dataKeySet.contains("ports"))
            this.processPortInfo(allDataJson, "ports", ip, date);

        return Result.successWithMessage("成功获取到全部数据");
    }


    public void updateHostInfo(JSONObject jsonObject) {

        Host host = new Host();
        host.setIp(jsonObject.getStr("ip"));
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        host.setDateHost(date);
        QueryWrapper<Host> hostQueryWrapper = new QueryWrapper<>();
        hostQueryWrapper.eq("ip", host.getIp());
        Host queryHost = iHostsService.getOne(hostQueryWrapper);
        if (queryHost == null) {
            iHostsService.save(host);
            log.info("插入一条 Host 数据：{}", host.toString());
        }else{
            queryHost.setDateHost(host.getDateHost());
            iHostsService.updateById(queryHost);
            log.info("更新一条 Host 数据：{}", queryHost.toString());
        }
    }


    public void processGpuInfo(JSONObject jsonObject, String fieldName) {
        System.out.println(jsonObject.get("ip"));
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        JSONArray allGPUInfo = jsonObject.get(fieldName, JSONArray.class);
        int gpu_count = allGPUInfo.size();
        for (int i = 0; i < gpu_count; i++) {
            JSONObject oneGPUInfo = allGPUInfo.getJSONObject(i);
            oneGPUInfo.putOnce("ip", ip);
            oneGPUInfo.putOnce("id_host", 12);
            oneGPUInfo.putOnce("date_gpu", date);
            GpuInfo oneGPUBean = oneGPUInfo.toBean(GpuInfo.class);
            iGpuInfoService.save(oneGPUBean);
            //log.info("插入一条 GPU 数据：{}", oneGPUBean.toString());
        }
    }

    /**
     * 处理 GPU 进程信息
     *
     * @param jsonObject
     */
    public void processGPUProcInfo(JSONObject jsonObject, String fieldName) {
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        JSONArray allGPUProcInfo = jsonObject.get(fieldName, JSONArray.class);
        for (int i = 0; i < allGPUProcInfo.size(); i++) {
            JSONObject oneGPUProcInfo = allGPUProcInfo.getJSONObject(i);
            GpuProc gpuProc = oneGPUProcInfo.toBean(GpuProc.class);
            gpuProc.setIp(ip);
            gpuProc.setDateGpuProc(date);
            iGpuProcService.save(gpuProc);
            //log.info("插入一条 GPU 进程数据：{}", gpuProc.toString());
        }
    }

    public void processDiskUsageInfo(JSONObject jsonObject, String fieldName) {
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        JSONArray usageInfos = jsonObject.get(fieldName, JSONArray.class);
        for (int i = 0; i < usageInfos.size(); i++){
            JSONObject oneUsageInfoJson = usageInfos.getJSONObject(i);
            DiskUsage diskUsage = oneUsageInfoJson.toBean(DiskUsage.class);
            diskUsage.setIp(ip);
            diskUsage.setDateDisk(date);
            iDiskUsageService.save(diskUsage);
            //log.info("插入一条硬盘用量信息：{}", diskUsage);
        }
    }




    public void processMemoryInfo(JSONObject jsonObject, String fieldName , String ip, Timestamp date){

        JSONObject memoryInfo = jsonObject.get(fieldName, JSONObject.class);
        Memory memory = memoryInfo.toBean(Memory.class);
        memory.setIp(ip);
        memory.setDate(date);
        iMemoryService.save(memory);
        //log.info("插入一条内存使用：{}", memory);
    }

    public void processPortInfo(JSONObject jsonObject, String fieldName,String ip, Timestamp date) {

        JSONArray portInfos = jsonObject.get(fieldName, JSONArray.class);
        for (int i = 0; i < portInfos.size(); i++){
            JSONObject oneUsageInfoJson = portInfos.getJSONObject(i);
            Port portInfo = oneUsageInfoJson.toBean(Port.class);
            portInfo.setIp(ip);
            portInfo.setDate(date);
            //iDiskUsageService.save(diskUsage);
            iPortService.save(portInfo);
            //log.info("插入一条端口信息：{}", portInfo);
        }
    }

}
