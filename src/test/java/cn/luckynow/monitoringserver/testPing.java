package cn.luckynow.monitoringserver;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;

/**
 * @description:
 * @author：xz
 * @date: 2023/8/15
 */
public class testPing {
    @Test
    public void test01() {
        String ipAddress = "125.216.243.1"; // 要ping的IP地址
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            boolean reachable = inetAddress.isReachable(5000); // 设置超时时间为5秒
            if (reachable) {
                System.out.println("IP地址 " + ipAddress + " 可达。");
            } else {
                System.out.println("IP地址 " + ipAddress + " 不可达。");
            }
        } catch (Exception e) {
            System.err.println("发生异常：" + e.getMessage());
        }
    }
}
