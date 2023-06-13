package tech.wedev.wecom.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource(value = "classpath:META-INF/global-config.properties")
public class NosUtil {
    private static String host;
    private static int port;
    private static int timeout;
    private static String appId;
    private static String passPort;

    @Value("${nos.host}")
    public void setHost(String host) {
        NosUtil.host = host;
    }

    @Value("${nos.port}")
    public void setPort(int port) {
        NosUtil.port = port;
    }

    @Value("${nos.timeout}")
    public void setTimeout(int timeout) {
        NosUtil.timeout = timeout;
    }

    @Value("${nos.appId}")
    public void setAppId(String appId) {
        NosUtil.appId = appId;
    }

    @Value("${nos.passPort}")
    public void setPassPort(String passPort) {
        NosUtil.passPort = passPort;
    }

    public static JnosRedisPool getInstance() {
        //如应用功能测试环境，设置前缀
        Map<String, String> cfg = new HashMap<>();
        cfg.put("prefix", "cfbi");

        //目前针对SDK2.5版本连接池，所有参数都已进行封装，无需应用设置，应用如有自己设置的需求，请联系nos项目组。
        //连接池，一般设置为单例模式，连接池已封装自动归还连接
        return JnosRedisPool.getInstance(cfg, host, port, timeout, appId, passPort);
    }

}
