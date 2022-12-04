package cn.luckynow.monitoringserver.test;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import cn.luckynow.monitoringserver.service.IHostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IGpuInfoService iGpuInfoService;

    @Autowired
    private IHostsService iHostsService;


    @PostMapping("/host/info")
    public String receiveHostInfo(@RequestBody String hostInfo){
        JSONObject jsonObject = JSONUtil.parseObj(hostInfo);
        Host host = jsonObject.toBean(Host.class);

        iHostsService.save(host);
        System.out.println(123);
        return "receive host info success";
    }



    /**
     * 接受
     * @return
     */
    @PostMapping("/gpu/info")
    public String receiveGPUInfo(@RequestBody String gpu_info){

        System.out.println(gpu_info);
        JSONObject jsonObject = JSONUtil.parseObj(gpu_info);
        System.out.println(jsonObject.get("ip"));
        String ip = jsonObject.get("ip", String.class);
        Timestamp date = new Timestamp(jsonObject.get("date", Long.class) * 1000);
        JSONArray allGPUInfo = jsonObject.get("gpuInfo", JSONArray.class);
        int gpu_count = allGPUInfo.size();
        for( int i=0; i<gpu_count; i++){
            JSONObject oneGPUInfo = allGPUInfo.getJSONObject(i);
            oneGPUInfo.putOnce("ip", ip);
            oneGPUInfo.putOnce("id_host", 12);
            oneGPUInfo.putOnce("date_gpu", date);
            //oneGPUInfo.putOnce("index_gpu", i);
            //oneGPUInfo.putOnce("count_gpu", gpu_count);
            GpuInfo oneGPUBean = oneGPUInfo.toBean(GpuInfo.class);
            iGpuInfoService.save(oneGPUBean);
            System.out.println(121233);
        }

        System.out.println("finished");
        return "receive gpu info success";
    }

}
