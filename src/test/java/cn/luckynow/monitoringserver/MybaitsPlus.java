package cn.luckynow.monitoringserver;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
public class MybaitsPlus {

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
                    builder.parent("cn.luckynow.monitoringserver") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\Desktop\\xz")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("docker_info")// 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
