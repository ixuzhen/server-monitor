package cn.luckynow.monitoringserver.util;

public class CommonUtils {

    public static String byteToKbMbGbTb(long byteSize) {
        double kb = 1024.0;
        double mb = kb * 1024;
        double gb = mb * 1024;
        double tb = gb * 1024;
        if (byteSize < kb) {
            return byteSize + "B";
        } else if (byteSize < mb) {
            return String.format("%.2f",byteSize / kb) + "KB";
        } else if (byteSize < gb) {
            return String.format("%.2f",byteSize / mb) + "MB";
        } else if (byteSize < tb) {
            return String.format("%.2f",byteSize / gb) + "GB";
        } else {
            return String.format("%.2f",byteSize / tb) + "TB";
        }
    }

}
