package tech.wedev.wecom.utils;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class MapUtils{
    private MapUtils(){
        /**工具类，不实例化 **/
    }

    //将Map集合中的数据封装到JavaBean对象中
    public static <T> T map2bean(Map<String,Object> map, Class<T> classType){
        map.put("wfFlowNo", map.get("wf_flowNo"));
        map.put("wfTodo", map.get("wf_todo"));
        try{
            T obj = classType.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(classType, Object.class);
            /** 获取bean对象中的所有属性 **/
            PropertyDescriptor[] list = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : list){
                //获取属性名
                String key = pd.getName();
                //获取属性值
                Object value = map.get(key);
                writeValue(obj, pd, key, value);
            }
            return obj;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private static <T> void writeValue(T obj, PropertyDescriptor pd, String key, Object value){
        try {
            //调用属性setter()方法，设置到javabean对象当中
            pd.getWriteMethod().invoke(obj, value);
        }catch (Exception e){
            log.error("复制值失败，key {}, value {}", key, value, e);
        }
    }

    /**
     * 将javabean对象封装到Map集合中
     */
    public static Map<String, Object> bean2map(Object bean){
        try{
            //创建Map集合对象
            Map<String, Object> map = new HashMap<>(16);
            //获取对象字节码信息，不要Object的属性
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
            //获取bean对象中的所有属性
            PropertyDescriptor[] list = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor pd : list){
                //获取属性名
                String key = pd.getName();
                //调用getter()方法，获取内容
                Object value = pd.getReadMethod().invoke(bean);
                //增加到map集合当中
                map.put(key, value);
            }
            return map;
        }catch (Exception e){
            log.error("bean2map {}", e.getMessage(), e);
            throw new BeanMapConvertException(e.getMessage(), e);
        }
    }

    public static final class BeanMapConvertException extends RuntimeException{
        private static final long serialVersionUID = 1L;

        public BeanMapConvertException(){
        }

        public BeanMapConvertException(String message){
            super(message);
        }

        public BeanMapConvertException(String message, Throwable cause){
            super(message, cause);
        }

        public BeanMapConvertException(Throwable cause){
            super(cause);
        }

        public BeanMapConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
