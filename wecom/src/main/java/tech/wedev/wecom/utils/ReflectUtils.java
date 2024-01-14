package tech.wedev.wecom.utils;

import lombok.SneakyThrows;
import tech.wedev.wecom.enums.DefaultValEnum;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectUtils {
    private static final String SET_METHOD_PREFIX = "set";
    private static final String GET_METHOD_PREFIX = "get";
    private static final String METHOD_SPLITER = "\\.";
    private static final String ARRAY_METHOD_SPLITER = "]";
    private static final String ARRAY_METHOD_REGEX = "\\d+\\]";
    private static final String LIST_METHOD_SPLITER = "}";
    private static final String LIST_METHOD_REGEX = "\\d+\\}";
    private static final String MAP_METHOD_SPLITER = ")";

    private ReflectUtils() {

    }

    /**
     * @param src
     * @param
     * @param <T>
     * @return
     */
    public static <T> T getMethodByExpression(Object src, String getMethodNameExpression) {
        RuntimeExceptionUtils.isTrue(StringUtil.isBlank(getMethodNameExpression), StringUtil.format("表达式不能为空，getMethodNameExpression{}", getMethodNameExpression));
        RuntimeExceptionUtils.isTrue(src == null, StringUtil.format("对象不能为空"));
        final List<String> methodNames = StringUtil.split(getMethodNameExpression, METHOD_SPLITER);
        Object invokeObj = src;
        String getMethodName = methodNames.get(0);
        if (StringUtil.matchOne(getMethodName, ARRAY_METHOD_REGEX).length() > 1) {
            final String[] split = getMethodName.split(ARRAY_METHOD_SPLITER);
            invokeObj = invokeGet(invokeObj, split[1]);
            if (invokeObj == null) {
                return null;
            }
            invokeObj = ((Object[]) invokeObj)[IntegerUtils.valueOf(split[0])];
        } else if (StringUtil.matchOne(getMethodName, LIST_METHOD_REGEX).length() > 1) {
            final String[] split = getMethodName.split(LIST_METHOD_SPLITER);
            invokeObj = invokeGet(invokeObj, split[1]);
            if (invokeObj == null) {
                return null;
            }
            invokeObj = ((List) invokeObj).get(IntegerUtils.valueOf(split[0]));
        } else {
            invokeObj = invokeGet(invokeObj, getMethodName);
        }
        if (methodNames.size() > 1 && invokeObj != null) {
            return getMethodByExpression(invokeObj, StringUtil.join(methodNames.subList(1, methodNames.size()), "."));
        }
        return (T) invokeObj;
    }

    public static <T> void setMethodByExpression(Object src, String setMethodNameExpression, Class<T> parameterType, T parameter) {
        RuntimeExceptionUtils.isTrue(StringUtil.isBlank(setMethodNameExpression), StringUtil.format("表达式不能为空，setMethodNameExpression{}", setMethodNameExpression));
        RuntimeExceptionUtils.isTrue(src == null, StringUtil.format("对象不能为空"));
        final List<String> methodNames = StringUtil.split(setMethodNameExpression, METHOD_SPLITER);
        String getMethodName = methodNames.get(0);
        Object invokeObj = src;
        if (StringUtil.matchOne(getMethodName, ARRAY_METHOD_REGEX).length() > 1) {
            invokeObj = getObjectForArray(src, getMethodName, invokeObj);
        } else if (StringUtil.matchOne(getMethodName, LIST_METHOD_REGEX).length() > 1) {
            invokeObj = getObjectForList(src, getMethodName, invokeObj);
        } else {
            invokeObj = invokeGet(invokeObj, getMethodName);
            if (invokeObj == null) {
                invokeObj = getReturnTypeObjectByMethod(src, getMethodName);
            }
            invokeSet(src, getMethodName, (Class<Object>) invokeObj.getClass(), methodNames.size() > 1 ? invokeObj : parameter);
        }
        if (methodNames.size() > 1 && invokeObj != null) {
            setMethodByExpression(invokeObj, StringUtil.join(methodNames.subList(1, methodNames.size()), "."), parameterType, parameter);
        }
    }

    private static Object getObjectForArray(Object src, String getMethodName, Object invokeObj) {
        final String[] split = getMethodName.split(ARRAY_METHOD_SPLITER);
        invokeObj = invokeGet(invokeObj, split[1]);
        int requestLength = IntegerUtils.valueOf(split[0]) + 1;
        if (invokeObj == null) {
            final Object[] temp = (Object[]) Array.newInstance(getGetterMethod(src.getClass(), split[1]).getReturnType(), requestLength);
            for (int i = 0; i < requestLength; i++) {
                temp[i] = getObjectByClazz(getGetterMethod(src.getClass(), split[1]).getReturnType().getComponentType());
            }
            invokeObj = temp;
        }
        Object[] invokeArray = (Object[]) invokeObj;
        final int length = invokeArray.length;
        if (requestLength > length) {
            invokeArray = Arrays.copyOf(invokeArray, requestLength);
            for (int i = length; i < requestLength; i++) {
                if (ArrayUtils.indexOf(invokeArray, i) == null) {
                    invokeArray[i] = getArrayObject(invokeArray);
                }
            }
        }
        invokeSet(src, split[1], (Class<Object[]>) invokeArray.getClass(), invokeArray);
        invokeObj = invokeArray[IntegerUtils.valueOf(split[0])];
        return invokeObj;
    }

    private static Object getObjectForList(Object src, String getMethodName, Object invokeObj) {
        final String[] split = getMethodName.split(LIST_METHOD_SPLITER);
        invokeObj = invokeGet(invokeObj, split[1]);
        if (invokeObj == null) {
            invokeObj = new ArrayList<>();
        }
        final List invokeObjList = (List) invokeObj;
        int requestLength = IntegerUtils.valueOf(split[0]) + 1;
        if (requestLength > invokeObjList.size()) {
            for (int i = invokeObjList.size(); i < requestLength; i++) {
                if (ListUtils.indexOf(invokeObjList, i) == null) {
                    invokeObjList.add(getGenericTypeFieldObject(src.getClass(), split[1]));
                }
            }
        }
        invokeSet(src, split[1], List.class, invokeObjList);
        invokeObj = invokeObjList.get(IntegerUtils.valueOf(split[0]));
        return invokeObj;
    }

    @SneakyThrows
    public static <T> T getObjectByClazz(Class<T> clazz) {
        return clazz.newInstance();
    }

    @SneakyThrows
    private static Object getArrayObject(Object[] objects) {
        return ArrayUtils.getArrayType(objects).newInstance();
    }

    private static Object getGenericTypeFieldObject(Class clazz, String getMethodName) {
        final ParameterizedType genericType = (ParameterizedType) getDeclaredField(clazz, getMethodName).getGenericType();
        try {
            return ((Class) genericType.getActualTypeArguments()[0]).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class getGenericTypeFieldClass(Class clazz, String field) {
        Field declaredFieldByAll = getDeclaredFieldByAll(clazz, field);
        if (declaredFieldByAll != null) {
            String s = declaredFieldByAll.toGenericString();
            if (s.indexOf("<") != -1) {
                final ParameterizedType genericType = (ParameterizedType) declaredFieldByAll.getGenericType();
                return (Class) genericType.getActualTypeArguments()[0];
            }
        }
        return null;
    }

    private static Object getReturnTypeObjectByMethod(Object src, String getMethodName) {
        final Method declaredMethod = getGetterMethod(src.getClass(), getMethodName);
        try {
            return declaredMethod.getReturnType().newInstance();
        } catch (InstantiationException e) {
            return DefaultValEnum.getByCode(getGetterMethod(src.getClass(), getMethodName).getReturnType()).getDefaultVal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeGet(Object src, String getMethodName) {
        final Method getterMethod = getGetterMethod(src.getClass(), getMethodName);
        try {
            return (T) getterMethod.invoke(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void invokeSet(Object src, String setMethodName, Class<T> parameterType, T parameter) {
        final Method setterMethod = getSetterMethod(src.getClass(), setMethodName, parameterType);
        try {
            setterMethod.invoke(src, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getDeclaredField(Class clazz, String fieldName) {
        final Field declaredField;
        try {
            declaredField = clazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getDeclaredFieldByAll(Class clazz, String fieldname) {
        List<Field> declaredFields = getDeclaredFields(clazz);
        return declaredFields.stream().filter(a -> a.getName().equals(fieldname)).map(a -> {
            a.setAccessible(true);
            return a;
        }).findFirst().orElse(null);
    }

    private static Method getGetterMethod(Class clazz, String getMethodName) {
        return getDeclaredMethod(clazz, GET_METHOD_PREFIX + StringUtil.capitalizeFirstLetter(getMethodName), null);
    }

    private static Method getSetterMethod(Class clazz, String getMethodName, Class<?> parameterTye) {
        return getDeclaredMethod(clazz, SET_METHOD_PREFIX + StringUtil.capitalizeFirstLetter(getMethodName), parameterTye);
    }

    public static Method getDeclaredMethod(Class clazz, String methodName, Class<?>... parameterTypes) {
        final Method declaredMethod;
        try {
            declaredMethod = clazz.getDeclaredMethod(methodName, parameterTypes);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() == Object.class) {
                throw new RuntimeException(e);
            }
            return getDeclaredMethod(clazz.getSuperclass(), methodName, parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeDeclareMethod(Object src, Method method, Class<T> returnType, Object... params) {
        try {
            return (T) method.invoke(src, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeDeclaredMethod(Object src, Method method, Object... params) {
        return invokeDeclareMethod(src, method, null, params);
    }

    public static <T> T invokeDeclareMethod(Object src, String methodName, Class<T> returnType, Object... params) {
        try {
            final Object[] objects = BeanUtils.defaultIfNull(params, new Object[]{});
            Class<?>[] cl = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                cl[i] = objects[i].getClass();
            }
            final Method declaredMethod = getDeclaredMethod(src.getClass(), methodName, cl);
            return (T) declaredMethod.invoke(src, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeDeclaredMethod(Object src, String methodName, Object... params) {
        return invokeDeclaredMethod(src, methodName, null, params);
    }

    public static List<String> getDeclaredMethodNames(Class src) {
        List<String> names = new ArrayList<>();
        if (src == Object.class) {
            return names;
        } else {
            names.addAll(Arrays.stream(src.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList()));
            names.addAll(getDeclaredMethodNames(src.getSuperclass()));
            return names;
        }
    }

    @SneakyThrows
    public static Class invokeField(Object src, String fieldName, Object val) {
        final Field decalredfield = src.getClass().getDeclaredField(fieldName);
        decalredfield.setAccessible(true);
        decalredfield.set(src, val);
        return decalredfield.getType();
    }

    public static boolean isSuperInterface(Class clazz, Class interfaceClass) {
        Class[] interfaces = clazz.getInterfaces();
        for (Class anInterface : interfaces) {
            if (anInterface == interfaceClass) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSuperInterfaceAll(Class clazz, Class interfaceClass) {
        if (clazz == Object.class) {
            return false;
        }
        Class[] interfaces = clazz.getInterfaces();
        for (Class anInterface : interfaces) {
            if (anInterface == interfaceClass) {
                return true;
            }
        }
        return isSuperInterface(clazz.getSuperclass(), interfaceClass);
    }

    public static List<Field> getDeclaredFields(Class src) {
        List<Field> names = new ArrayList<>();
        if (src == Object.class) {
            return names;
        } else {
            names.addAll(Arrays.stream(src.getDeclaredFields()).map(a -> {
                a.setAccessible(true);
                return a;
            }).collect(Collectors.toList()));
            names.addAll(getDeclaredFields(src.getSuperclass()));
            return names;
        }
    }

    public static List<Method> getDeclaredMethods(Class src) {
        List<Method> names = new ArrayList<>();
        if (src == Object.class) {
            return names;
        } else {
            names.addAll(Arrays.stream(src.getDeclaredMethods()).map(a -> {
                a.setAccessible(true);
                return a;
            }).collect(Collectors.toList()));
            names.addAll(getDeclaredMethods(src.getSuperclass()));
            return names;
        }
    }

    public static Field getDeclaredRootField(Class src, String fieldName) {
        if (src == Object.class) {
            return null;
        } else {
            try {
                return src.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                return getDeclaredRootField(src.getSuperclass(), fieldName);
            }
        }
    }

    public static Field getDeclaredFieldAll(Class clazz, String fieldName) {
        List<Field> declaredFields = ReflectUtils.getDeclaredFields(clazz);
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().equals(fieldName)) {
                return declaredField;
            }
        }
        return null;
    }
}
