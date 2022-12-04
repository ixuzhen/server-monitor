package cn.luckynow.monitoringserver;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.GpuInfo;
import cn.luckynow.monitoringserver.entity.Host;
import cn.luckynow.monitoringserver.service.IGpuInfoService;
import cn.luckynow.monitoringserver.service.IHostsService;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.Collections;

@SpringBootTest
class MonitoringServerApplicationTests {


    @Autowired
    private IGpuInfoService iGpuInfoService;

    @Autowired
    private IHostsService iHostsService;

    /**
     *
     */



    @Test
    void testGPU(){
        GpuInfo gpuInfo = new GpuInfo();
        gpuInfo.setIdHost(new Long(123));
        gpuInfo.setIp("125.216.243.209");
        gpuInfo.setIndexGpu(0);
        gpuInfo.setCountGpu(4);
        gpuInfo.setDateGpu(new Timestamp(new Long(1670045423)));
        iGpuInfoService.save(gpuInfo);
    }

    @Test
    void testDate() {
        String dateStr = "2022-12-02 15:54:10";
        DateTime date = DateUtil.parse(dateStr);
        String formatDateTime = DateUtil.formatDateTime(date);
        System.out.println(formatDateTime);
    }

    /**
     * 使用 mybatis-plus 逆向工程来生成代码
     */
    @Test
    void generator() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/monitoring", "root",
                        "123456")
                .globalConfig(builder -> {
                    builder.author("xz") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\Desktop\\xz"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.luckynow") // 设置父包名
                            .moduleName("monitoringserver") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\Desktop\\xz")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("gpu_info","hosts")// 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
