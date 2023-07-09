package tech.wedev.wecom.utils;

import lombok.extern.slf4j.Slf4j;
import tech.wedev.util.snowflake.SnowFlake;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 雪花算法工具类
 */
@Slf4j
public class SnowFlakeUtil {
    public SnowFlakeUtil() {
    }

    private static class SnowFlakeHolder {
        private static SnowFlake INSTANCE;

        static {
            try {
                String ip = getRealIp();
                log.info(String.format("The real ip is %s", ip));
                int hash = ip.hashCode();
                int machineId = 255 & ((hash ^ (hash >>> 16)) & 0x7fffffff);

                machineId = (machineId > 31 || machineId < 0) ? 1 : machineId;
                log.info(String.format("The machine Id is %d", machineId));

                INSTANCE = new SnowFlake(machineId, 1L);
            } catch (Exception e) {
                log.error("初始化SnowFlake错误: ", e);
            }
        }
    }

    public static SnowFlake getInStance() {
        return SnowFlakeHolder.INSTANCE;
    }

    public static String getNextLongId() {
        return SnowFlakeHolder.INSTANCE.nextLongId();
    }

    /**
     * 获取本机IP
     */
    private static String getRealIp() {
        //本地IP，如果没有配置外网IP则返回它
        String localIP = null;
        //外网IP
        String netIP = null;

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean find = false;
            while (networkInterfaces.hasMoreElements() && !find) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> address = networkInterface.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();

                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                        continue;
                    }

                    localIP = ip.getHostAddress();
                    if (!ip.isSiteLocalAddress()) {
                        netIP = ip.getHostAddress();
                        find = true;
                    }
                }
            }
            return StringUtils.isNotBlank(netIP) ? netIP : localIP;
        } catch (Exception e) {
            log.error("解析IP错误: {}", e);
            return "127.0.0.1";
        }
    }

    public static void main(String[] args) {
        System.out.println(SnowFlakeHolder.INSTANCE.nextLongId());
        System.out.println(SnowFlakeUtil.getNextLongId());
    }
}
