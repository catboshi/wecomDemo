package tech.wedev.wecom.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    /**
     * 实体对象转Map
     */
    public static Map<String, Object> object2Map(Object obj){
        Map<String,Object> map = new HashMap<>();
        if (obj == null){
            return map;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try{
            for(Field field : fields){
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
