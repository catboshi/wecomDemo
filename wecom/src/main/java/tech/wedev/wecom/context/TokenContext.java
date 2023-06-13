package tech.wedev.wecom.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token上下文
 */
public class TokenContext {
    private final Map<String, Object> innerMap = new ConcurrentHashMap<>();

    public TokenContext() {

    }

    public TokenContext(Map<String, Object> map) {
        if (map!=null) {
            for (Map.Entry<String, Object> m : map.entrySet()) {
                String key = m.getKey();
                Object val = m.getValue() == null ? "" : m.getValue();
                innerMap.put(key, val);
            }
        }
    }

    public Object getKey(String key) {
        return innerMap.get(key);
    }
}
