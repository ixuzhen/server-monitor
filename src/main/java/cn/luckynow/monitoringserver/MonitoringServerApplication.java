package cn.luckynow.monitoringserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("cn.luckynow.monitoringserver.mapper")
@EnableScheduling
public class MonitoringServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringServerApplication.class, args);
    }

}
