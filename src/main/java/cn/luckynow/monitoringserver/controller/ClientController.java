package cn.luckynow.monitoringserver.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/client")
@Slf4j
@CrossOrigin
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IDockerInfoService iDockerInfoService;


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
        if (dataKeySet.contains("docker_info"))
            this.processDockerInfo(allDataJson, "docker_info", ip, date);

        return Result.successWithMessage("成功获取到全部数据");
    }



    /**
     * 获取心跳数据，存入Redis中
     * @param heartBeatData
     */
    @PostMapping("/heartbeat")
    public Result<Object> receiveHeartBeat(@RequestBody String heartBeatData) {
        JSONObject jsonObject = JSONUtil.parseObj(heartBeatData);
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        String key = RedisConstants.HEART_BEAT_KEY + ip;
        stringRedisTemplate.opsForValue().set(key, String.valueOf(date.getTime()));
        stringRedisTemplate.expire(key, RedisConstants.HEART_BEAT_TTL, java.util.concurrent.TimeUnit.MINUTES);
        log.info("成功获取到心跳数据：{}", heartBeatData);
        return Result.successWithMessage("成功获取到心跳数据");
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
            if (oneGPUProcInfo.get("startTime") != null) {
                // 将上海时区的时间格式'Fri Jul 21 18:22:29 2023'，转成Timestamp
                String dateString = oneGPUProcInfo.getStr("startTime");
                if(dateString.charAt(8) == ' ') {
                    dateString = dateString.substring(0, 8) + "0" + dateString.substring(9);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
                try {
                    LocalDateTime parsedDateTime = LocalDateTime.parse(dateString, formatter);
                    Timestamp timestamp = Timestamp.from(parsedDateTime.toInstant(java.time.ZoneOffset.ofHours(8)));
                    oneGPUProcInfo.set("startTime", timestamp);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
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

    private void processDockerInfo(JSONObject allDataJson, String docker_info, String ip, Timestamp date) {
        JSONArray dockerInfos = allDataJson.get(docker_info, JSONArray.class);
        for (int i = 0; i < dockerInfos.size(); i++){
            JSONObject oneUsageInfoJson = dockerInfos.getJSONObject(i);
            DockerInfo docker = oneUsageInfoJson.toBean(DockerInfo.class);
            docker.setIp(ip);
            docker.setDate(date);
            iDockerInfoService.save(docker);
            //log.info("插入一条docker信息：{}", docker);
        }
    }

}
