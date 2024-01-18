package tech.wedev.wecom.mybatis.mapper.provider;

import cn.hutool.core.collection.CollectionUtil;
import lombok.SneakyThrows;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import tech.wedev.wecom.annos.*;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.entity.qo.BasicQO;
import tech.wedev.wecom.enums.BaseIntegerEnum;
import tech.wedev.wecom.enums.BaseStringEnum;
import tech.wedev.wecom.exception.ExceptionAssert;
import tech.wedev.wecom.handler.BaseIntegerEnumTypeHandler;
import tech.wedev.wecom.handler.BaseStringEnumTypeHandler;
import tech.wedev.wecom.utils.BeanUtil;
import tech.wedev.wecom.utils.ReflectUtil;
import tech.wedev.wecom.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CommonProvider<P extends BasicPO, Q extends BasicQO> {
    private static final String PO_PACKAGE = "tech.wedev.wecom.entity.po";
    private static final String DATABASE_PREFIX = "zh";

    @SneakyThrows
    public String delete(@Param("q") Q q) {
        Class<? extends BasicQO> qClass = q.getClass();
        String tableName = this.getQOTableName(qClass);
        String sql = "delete from " + tableName + " ";
        sql = sql + this.getWhere(q, qClass);
        return sql;
    }

    @SneakyThrows
    public String batchDeleteByPrimaryKey(@Param("q") Q q) {
        Class<? extends BasicQO> qClass = q.getClass();
        ExceptionAssert.isNull(q.getIds(), "ids不能为空");
        String tableName = this.getQOTableName(qClass);
        String sql = "delete from " + tableName + " ";
        sql = sql + this.getWhere(q, qClass);
        return sql;
    }

    @SneakyThrows
    public String statistics(Q q) {
        Class<? extends BasicQO> qClass = q.getClass();
        String tableName = this.getQOTableName(qClass);
        String sql = "select " + q.getStatistics().getDesc() + "(`" + StringUtil.fieldNameToColumnName(q.getStatisticsField()) + "`) statistics" + " from " + tableName;
        sql = sql + this.getWhere(q, qClass);
        return sql;
    }

    public String batchInsert(@Param("ps") List<P> ps, @Param("p") Class<P> pClass) {
//        Class<? extends BasicPO> pClass = ps.get(0).getClass();
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(this.getPOTableName(pClass));
        List<Field> declaredFields = ReflectUtil.getDeclaredFields(pClass);
        List<String> columns = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        Set<String> databasePFields = this.getDatabasePFields(pClass);
        declaredFields = declaredFields.stream().filter(declaredField -> databasePFields.contains(declaredField.getName())).collect(Collectors.toList());
        for (Field declaredField : declaredFields) {
            Transient annotation = declaredField.getAnnotation(Transient.class);
            if (annotation != null) {
                continue;
            }
            if (declaredField.getName().equals("id")) {
                continue;
            }
            columns.add("`" + StringUtil.fieldNameToColumnName(declaredField.getName()) + "`");
            if (declaredField.getType().isEnum()) {
                if (ReflectUtil.isSuperInterface(declaredField.getType(), BaseIntegerEnum.class)) {
                    fields.add(" #'{'ps[{0}]." + declaredField.getName() + ",javaType=" + declaredField.getType().getName() + ",typeHandler=" + BaseIntegerEnumTypeHandler.class.getName() + "} ");
                } else if (ReflectUtil.isSuperInterface(declaredField.getType(), BaseStringEnum.class)) {
                    fields.add(" #'{'ps[{0}]." + declaredField.getName() + ",javaType=" + declaredField.getType().getName() + ",typeHandler=" + BaseStringEnumTypeHandler.class.getName() + "} ");
                }
            } else {
                fields.add("#'{'ps[{0}]." + declaredField.getName() + "}");
            }
        }

        sql.append(" (").append(StringUtil.join(columns, ",")).append(") ");
        sql.append(" values ");
        MessageFormat messageFormat = new MessageFormat("(" + StringUtil.join(fields, ",") + ")");
        for (int i = 0; i < ps.size(); i++) {
            sql.append(messageFormat.format(new Object[]{i}));
            if (i != ps.size() - 1) {
                sql.append(" , ");
            }
        }
        return sql.toString();
    }

    /**
     * 获取和表对应的字段
     */
    private Set<String> getDatabasePFields(Class<? extends BasicPO> p) {
        List<Field> declaredFields = ReflectUtil.getDeclaredFields(p);
        Set<String> removeFields = new HashSet<>();
        Set<String> databaseFields = new HashSet<>();
        for (Field declaredField : declaredFields) {
            Aliased annotation = declaredField.getAnnotation(Aliased.class);
            if (annotation != null) {
                removeFields.add(annotation.value());
            }
            databaseFields.add(declaredField.getName());
        }
        databaseFields.removeAll(removeFields);
        return databaseFields;
    }

    public String insert(@Param("p") P p) {
        Class<? extends BasicPO> pClass = p.getClass();
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(this.getPOTableName(pClass));
        List<Field> declaredFields = ReflectUtil.getDeclaredFields(pClass);
        List<String> columns = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        Set<String> databaseQFields = this.getDatabasePFields(pClass);
        declaredFields = declaredFields.stream().filter(declaredField -> databaseQFields.contains(declaredField.getName())).collect(Collectors.toList());
        for (Field declaredField : declaredFields) {
            Transient annotation = declaredField.getAnnotation(Transient.class);
            if (annotation != null) {
                continue;
            }
            if (declaredField.getName().equals("id")) {
                continue;
            }
            columns.add("`" + StringUtil.fieldNameToColumnName(declaredField.getName()) + "`");
            if (declaredField.getType().isEnum()) {
                if (ReflectUtil.isSuperInterface(declaredField.getType(), BaseIntegerEnum.class)) {
                    fields.add(" #{" + declaredField.getName() + ",typeHandler=" + BaseIntegerEnumTypeHandler.class.getName() + "} ");
                } else if (ReflectUtil.isSuperInterface(declaredField.getType(), BaseStringEnum.class)) {
                    fields.add(" #{" + declaredField.getName() + ",typeHandler=" + BaseStringEnumTypeHandler.class.getName() + "} ");
                }
            } else {
                fields.add("#{" + declaredField.getName() + "}");
            }
        }

        sql.append(" (").append(StringUtil.join(columns, ",")).append(") ");
        sql.append(" values(").append(StringUtil.join(fields, ",")).append(") ");
        return sql.toString();
    }

    @SneakyThrows
    public String updateSelective(@Param("q") Q q) {
        BasicPO updateValPO = q.getUpdateValPO();
        ExceptionAssert.isFalse(updateValPO.getClass().getSimpleName().replaceAll("PO", "QO").equals(q.getClass().getSimpleName()), "更新对象UpdateValPO与QO非共生体");
        ExceptionAssert.isNull(updateValPO, "更新实体不能为空");
        Class<? extends BasicQO> qClass = q.getClass();
        String tableName = this.getQOTableName(qClass);
        updateValPO.setId(q.getId());
        String sql = "update " + tableName + " " + this.getUpdateSelectiveSet(updateValPO) + " ";
        sql = sql + this.getWhere(q, qClass);
        return sql;
    }

    @SneakyThrows
    public String updateByPrimaryKey(@Param("q") Q q) {
        BasicPO updateValPO = q.getUpdateValPO();
        ExceptionAssert.isNull(q.getId(), "id不能为空");
        ExceptionAssert.isFalse(updateValPO.getClass().getSimpleName().replaceAll("PO", "QO").equals(q.getClass().getSimpleName()), "更新对象UpdateValPO与QO非共生体");
        ExceptionAssert.isNull(updateValPO, "更新实体不能为空");
        Class<? extends BasicQO> qClass = q.getClass();

        String tableName = this.getQOTableName(qClass);
        updateValPO.setId(q.getId());
        String sql = "update " + tableName + " " + this.getUpdateSelectiveSet(updateValPO) + " ";
        sql = sql + this.getWhere(q, qClass);
        return sql;
    }

    @SneakyThrows
    public String update(@Param("q") Q q) {
        BasicPO updateValPO = q.getUpdateValPO();
        ExceptionAssert.isFalse(updateValPO.getClass().getSimpleName().replaceAll("PO", "QO").equals(q.getClass().getSimpleName()), "更新对象UpdateValPO与QO非共生体");
        ExceptionAssert.isNull(updateValPO, "更新实体不能为空");
        Class<? extends BasicQO> qClass = q.getClass();

        String tableName = this.getQOTableName(qClass);
        updateValPO.setId(q.getId());
        String sql = "update " + tableName + " " + this.getUpdateSet(updateValPO) + " ";
        sql = sql + this.getWhere(q, qClass);
        return sql;
    }

    @SneakyThrows
    private String getUpdateSelectiveSet(BasicPO basicPO) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Method> declaredMethods = ReflectUtil.getDeclaredMethods(basicPO.getClass()).stream().filter(a -> a.getName().startsWith("get")).collect(Collectors.toList());
        String commaSymbol = " , ";
        Set<String> databaseQFields = this.getDatabasePFields(basicPO.getClass());
        declaredMethods = declaredMethods.stream().filter(declaredMethod -> databaseQFields.contains(StringUtil.lowerCaseFirstLetter(declaredMethod.getName().replaceAll("get", "")))).collect(Collectors.toList());
        for (Method declaredMethod : declaredMethods) {
            String fieldName = StringUtil.lowerCaseFirstLetter(declaredMethod.getName().replaceAll("get", ""));
            Object o = ReflectUtil.invokeGet(basicPO, fieldName);
            Field declaredField = ReflectUtil.getDeclaredFieldAll(basicPO.getClass(), fieldName);
            if ("id".equals(fieldName) && basicPO.getId() == null) {
                continue;
            }
            if (o == null) {
                continue;
            }
            if (declaredField.getAnnotation(Transient.class) != null) {
                continue;
            }
            if (o instanceof String) {
                if (StringUtil.isEmpty(o.toString())) {
                    continue;
                }
            }

            Class<?> returnType = declaredMethod.getReturnType();
            if (returnType.isEnum()) {
                if (ReflectUtil.isSuperInterface(returnType, BaseIntegerEnum.class)) {
                    stringBuilder.append("`" + StringUtil.fieldNameToColumnName(fieldName) + "`").append(" = ").append("#{updateValPO.").append(fieldName).append(",typeHandler=").append(BaseIntegerEnumTypeHandler.class.getName()).append("}").append(commaSymbol);
                } else if (ReflectUtil.isSuperInterface(returnType, BaseStringEnum.class)) {
                    stringBuilder.append("`" + StringUtil.fieldNameToColumnName(fieldName) + "`").append(" = ").append("#{updateValPO.").append(fieldName).append(",typeHandler=").append(BaseStringEnumTypeHandler.class.getName()).append("}").append(commaSymbol);
                    ;
                }
            } else {
                stringBuilder.append("`" + StringUtil.fieldNameToColumnName(fieldName) + "`").append(" = ").append("#{updateValPO.").append(fieldName).append("}").append(commaSymbol);
            }
        }
        String set = stringBuilder.toString();
        if (StringUtil.isNotEmpty(set)) {
            set = " set ".substring(0, set.length() - commaSymbol.length());
        }
        return set;
    }

    @SneakyThrows
    private String getUpdateSet(BasicPO basicPO) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Method> declaredMethods = ReflectUtil.getDeclaredMethods(basicPO.getClass()).stream().filter(a -> a.getName().startsWith("get")).collect(Collectors.toList());
        String commaSymbol = " , ";
        Set<String> databaseQFields = this.getDatabasePFields(basicPO.getClass());
        declaredMethods = declaredMethods.stream().filter(declaredMethod -> databaseQFields.contains(StringUtil.lowerCaseFirstLetter(declaredMethod.getName().replaceAll("get", "")))).collect(Collectors.toList());
        for (Method declaredMethod : declaredMethods) {
            String fieldName = StringUtil.lowerCaseFirstLetter(declaredMethod.getName().replaceAll("get", ""));
            Field declaredField = ReflectUtil.getDeclaredFieldAll(basicPO.getClass(), fieldName);
            if ("id".equals(fieldName) && basicPO.getId() == null) {
                continue;
            }
            if (declaredField.getAnnotation(Transient.class) != null) {
                continue;
            }
            Class<?> returnType = declaredMethod.getReturnType();
            if (returnType.isEnum()) {
                if (ReflectUtil.isSuperInterface(returnType, BaseIntegerEnum.class)) {
                    stringBuilder.append("`" + StringUtil.fieldNameToColumnName(fieldName) + "`").append(" = ").append("#{updateValPO.").append(fieldName).append(",typeHandler=").append(BaseIntegerEnumTypeHandler.class.getName()).append("}").append(commaSymbol);
                } else if (ReflectUtil.isSuperInterface(returnType, BaseStringEnum.class)) {
                    stringBuilder.append("`" + StringUtil.fieldNameToColumnName(fieldName) + "`").append(" = ").append("#{updateValPO.").append(fieldName).append(",typeHandler=").append(BaseStringEnumTypeHandler.class.getName()).append("}").append(commaSymbol);
                }
            } else {
                stringBuilder.append("`" + StringUtil.fieldNameToColumnName(fieldName) + "`").append(" = ").append("#{updateValPO.").append(fieldName).append("}").append(commaSymbol);
            }
        }
        String set = stringBuilder.toString();
        if (StringUtil.isNotEmpty(set)) {
            set = " set " + set.substring(0, set.length() - commaSymbol.length());
        }
        return set;
    }

    @SneakyThrows
    public String select(@Param("q") Q q) {
        Class<? extends BasicQO> qClass = q.getClass();

        String tableName = this.getQOTableName(qClass);
        String sql = "select " + this.getSelectColumnNames(q, this.getPO(qClass)) + " from " + tableName;
        sql = sql + this.getWhere(q, qClass);
        sql += this.getGroupBy(q);
        sql += this.getOrderBy(q);
        if (q.getPageSize() != null && q.getPageNum() != null) {
            sql += " limit " + (q.getPageNum() - 1) * q.getPageSize() + "," + q.getPageSize();
        }
        return sql;
    }

    private String getOrderBy(@Param("q") Q q) {
        String sql = " ";
        List<String> orderBys = q.getOrderBys();
        if (!CollectionUtils.isEmpty(orderBys)) {
            orderBys.stream().filter(StringUtil::isNotEmpty).collect(Collectors.toList());
            sql += " order by ";
            for (int i = 0; i < orderBys.size(); i++) {
                String orderBy = orderBys.get(i);
                if (orderBy.endsWith("_1")) {
                    sql += StringUtil.fieldNameToColumnName(orderBy.substring(0, orderBy.indexOf("_1"))) + " asc ";
                } else if (orderBy.endsWith("_0")) {
                    sql += StringUtil.fieldNameToColumnName(orderBy.substring(0, orderBy.indexOf("_0"))) + " desc ";
                } else {
                    sql += StringUtil.fieldNameToColumnName(orderBy) + " asc ";
                }
                if (i != orderBys.size() - 1) {
                    sql += " , ";
                }
            }
        }
        return sql;
    }

    @SneakyThrows
    public String selectPage(@Param("q") Q q) {
        Class<? extends BasicQO> qClass = q.getClass();

        String tableName = this.getQOTableName(qClass);
        String sql = "select SQL_CALC_FOUND_ROWS " + this.getSelectColumnNames(q, this.getPO(qClass)) + " from " + tableName;
        sql = sql + this.getWhere(q, qClass);
        sql += this.getGroupBy(q);
        sql += this.getOrderBy(q);
        if (q.getPageSize() != null && q.getPageNum() != null) {
            sql += " limit " + (q.getPageNum() - 1) * q.getPageSize() + "," + q.getPageSize();
        }
        return sql;
    }

    private String getGroupBy(Q q) {
        String groupBy = " ";
        List<String> groupBys = q.getGroupBys();
        if (!CollectionUtils.isEmpty(groupBys)) {
            groupBys.stream().filter(StringUtil::isNotEmpty).map(StringUtil::fieldNameToColumnName).collect(Collectors.toList());
            groupBy = " group by " + StringUtil.join(groupBys, ",");
            //TODO 在这里实现having  ComparisonOperatorEnum
        }
        return groupBy;
    }

    public String getTotal() {
        return "SELECT FOUND_ROWS() AS total; ";
    }

    private String getWhere(@Param("q") Q q, Class<? extends BasicQO> qClass) throws IllegalAccessException, InvocationTargetException {
        StringBuilder where = new StringBuilder();
        List<Method> declaredMethods = ReflectUtil.getDeclaredMethods(qClass).stream().filter(a -> a.getName().startsWith("get")).collect(Collectors.toList());
        Map<String, Object> eqFields = new HashMap<>();
        Map<String, List<Object>> inFields = new HashMap<>();

        String andStr = " and ";
        Map<String, Object> scopeFields = new HashMap<>();
        for (Method method : declaredMethods) {
            Object val = method.invoke(q);
            Class<?> returnType = method.getReturnType();
            String fieldName = StringUtil.lowerCaseFirstLetter(method.getName().replaceAll("get", ""));
            if (val == null) {
                continue;
            }
            Field declaredField = ReflectUtil.getDeclaredRootField(qClass, fieldName);
            if (declaredField == null || declaredField.getAnnotation(NotWhere.class) != null) {
                continue;
            }
            if (val instanceof String) {
                if (StringUtil.isEmpty(val.toString())) {
                    continue;
                }
            }
            if (val instanceof List) {
                if (CollectionUtils.isEmpty((List) val)) {
                    continue;
                }
                inFields.put(fieldName.substring(0, fieldName.length() - 1), (List<Object>) val);
                continue;
            }

//            String name = method.getName();
            if (StringUtil.endsWith(fieldName, "Start", "End")) {
                scopeFields.put(fieldName, " #{" + fieldName + "} ");
            } else {
                String key = " `" + StringUtil.fieldNameToColumnName(fieldName) + "` ";
                if (returnType.isEnum()) {
                    if (ReflectUtil.isSuperInterface(returnType, BaseIntegerEnum.class)) {
                        eqFields.put(key, " #{" + fieldName + ",typeHandler=" + BaseIntegerEnumTypeHandler.class.getName() + "} ");
                    } else if (ReflectUtil.isSuperInterface(returnType, BaseStringEnum.class)) {
                        eqFields.put(key, " #{" + fieldName + ",typeHandler=" + BaseStringEnumTypeHandler.class.getName() + "} ");
                    }
                } else {
                    eqFields.put(key, " #{" + fieldName + "} ");
                }
            }
        }

        for (Map.Entry<String, List<Object>> stringObjectEntry : inFields.entrySet()) {
            where.append(andStr).append("`").append(StringUtil.fieldNameToColumnName(stringObjectEntry.getKey())).append("`").append(" in (");
            List<Object> value = stringObjectEntry.getValue();
            for (int i = 0; i < value.size(); i++) {
                Object obj = value.get(i);
                if (obj instanceof Integer || obj instanceof Long || obj instanceof Short) {
                    where.append(obj);
                } else {
                    where.append("'").append(obj).append("'");
                }
                if (i != value.size() - 1) {
                    where.append(",");
                }
            }
            where.append(" ) ");
        }
        for (Map.Entry<String, Object> stringObjectEntry : eqFields.entrySet()) {
            where.append(andStr).append(stringObjectEntry.getKey()).append(" = ").append(stringObjectEntry.getValue());
        }

        for (Map.Entry<String, Object> stringObjectEntry : scopeFields.entrySet()) {
            String key = stringObjectEntry.getKey();
            if (key.endsWith("Start")) {
                where.append(andStr).append(" `" + StringUtil.fieldNameToColumnName(key.substring(0, key.indexOf("Start"))) + "` ").append(" >= ").append(stringObjectEntry.getValue());
            } else {
                where.append(andStr).append(" `" + StringUtil.fieldNameToColumnName(key.substring(0, key.indexOf("End"))) + "` ").append(" <= ").append(stringObjectEntry.getValue());
            }
        }

        String s = where.toString();
        if (StringUtil.isNotEmpty(s)) {
            s = s.substring(andStr.length());

            //字段排序
            List<String> fieldOrders = BeanUtil.defaultIfNull(q.getFieldOrders(), new ArrayList<>());
            Map<String, Integer> fieldOrderMap = new HashMap<>();
            for (int i = 0; i < fieldOrders.size(); i++) {
                fieldOrderMap.put(fieldOrders.get(i), i);
            }
            String[] split = s.split(andStr);
            List<String> whereSort = Arrays.stream(split).sorted((a, b) -> {
                String aField = StringUtil.matchOne(a, "`\\w+`");
                aField = aField.substring(1, aField.length() - 1);
                String bField = StringUtil.matchOne(b, "`\\w+`");
                bField = bField.substring(1, bField.length() - 1);
                Integer aIndex = fieldOrderMap.get(StringUtil.columnNameToFieldName(aField));
                aIndex = BeanUtil.defaultIfNull(aIndex, 9999);
                Integer bIndex = fieldOrderMap.get(StringUtil.columnNameToFieldName(bField));
                bIndex = BeanUtil.defaultIfNull(bIndex, 9999);
                return aIndex - bIndex;
            }).collect(Collectors.toList());

            s = " where " + CollectionUtil.join(whereSort, " " + andStr + " ");

        }
        return s;
    }

    @SneakyThrows
    private Class<?> getPO(Class<? extends BasicQO> qClass) {
        return Class.forName(PO_PACKAGE + "." + qClass.getSimpleName().replaceAll("QO", "PO"));
    }

    private String getSelectColumnNames(@Param("q") Q q, Class<?> po) {
        Set<String> databasePFields = this.getDatabasePFields((Class<? extends BasicPO>) po);
        List<Field> declaredFields = ReflectUtil.getDeclaredFields(po);
        declaredFields = declaredFields.stream().filter(field -> databasePFields.contains(field.getName())).collect(Collectors.toList());
        List<String> columns = declaredFields.stream().filter(field -> field.getAnnotation(Transient.class) == null).map(field -> {
            TableField annotation = field.getAnnotation(TableField.class);
            String columnName;
            if (annotation != null && StringUtil.isNotEmpty(annotation.value())) {
                columnName = annotation.value();
            } else {
                columnName = this.getSelectColumn(field, q.getFields(), q.getIsExcludeFields());
            }
            return columnName;
        }).filter(a -> StringUtil.isNotEmpty(a)).map(a -> "`" + a + "`").collect(Collectors.toList());
        List<String> columnsMap = columns.stream().map(a -> {
            String o = a;
            String orginFieldsName = o.replace("`", "");
            if (orginFieldsName.indexOf("_") != -1) {
                String s = StringUtil.columnNameToFieldName(orginFieldsName);
                o += " " + s;
            }
            return o;
        }).collect(Collectors.toList());
        ExceptionAssert.isTrue(CollectionUtils.isEmpty(columnsMap), "列数不能为空");

        return StringUtil.join(columnsMap, " , ");
    }

    private String getPOTableName(Class<? extends BasicPO> pClass) {
        String tableName = StringUtil.fieldNameToColumnName(StringUtil.lowerCaseFirstLetter(pClass.getSimpleName()).replaceAll("PO", ""));
        return this.getTableName(pClass, tableName);
    }

    private String getTableName(Class<?> clazz, String tableName) {
        TableName annotation = clazz.getAnnotation(TableName.class);
        String databasePrefix = DATABASE_PREFIX + "_" + tableName;
        if (annotation != null) {
            databasePrefix = annotation.value();
        }
        return databasePrefix;
    }

    private String getQOTableName(Class<? extends BasicQO> qClass) {
        String tableName = StringUtil.fieldNameToColumnName(StringUtil.lowerCaseFirstLetter(qClass.getSimpleName()).replaceAll("QO", ""));
        return this.getTableName(qClass, tableName);
    }

    private String getSelectColumn(Field field, List<String> fields, Boolean isExcludeFields) {
        TableField annotation = field.getAnnotation(TableField.class);
        String columnName;
        if (annotation != null && StringUtil.isNotEmpty(annotation.value())) {
            columnName = annotation.value();
        } else {
            columnName = StringUtil.fieldNameToColumnName(field.getName());
        }
        if (!CollectionUtils.isEmpty(fields)) {
            fields = fields.stream().map(a -> StringUtil.fieldNameToColumnName(a)).collect(Collectors.toList());
            if (isExcludeFields != null) {
                if (isExcludeFields) {
                    //包含fields则排除
                    if (fields.contains(columnName)) {
                        return null;
                    }
                } else {
                    //不包含fields则排除
                    if (!fields.contains(columnName)) {
                        return null;
                    }
                }
            }
        }
        return columnName;
    }
}
