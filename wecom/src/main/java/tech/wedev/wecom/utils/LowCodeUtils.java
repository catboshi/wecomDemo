package tech.wedev.wecom.utils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.container.Triplet;
import tech.wedev.wecom.exception.ExceptionAssert;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class LowCodeUtils {
    private static final Map<String, String> jdbcToJavaTypeMap = new HashMap<>();

    //项目路径
    private static final String projectPath = System.getProperty("user.dir");
    private static final String blankChar = " ";
    private static final String xmlBlankChar = "    ";
    private static final String tabChar = "    ";
    /*
    private static final String commonPathRoot = projectPath + "/" + "wecom-common" + "/" + "src/main/java";
    private static final String enumPath = commonPathRoot + "/" + "com/icbc/cfbi/wecom/common/enums";
    private static final String servicePathClassRoot = projectPath + "/" + "wecom-online-gch-parent" + "/" + "wecom-ontine-gch-service";
    private static final String servicePathRoot = servicePathClassRoot + "/" + "src/main/java";
    private static final String poPath = commonPathRoot + "/" + "com/icbc/cfbi/wecom/common/basic/entity/po";
    private static final String qoPath = commonPathRoot + "/" + "com/icbc/cfbi/wecom/common/basic/entity/qo";
    private static final String mapperPath = servicePathRoot + "/" + "com/icbc/cfbi/wecom/online/dao/basic";
    private static final String mapperXmlPath = servicePathClassRoot + "/" + "src/main/resources/mybatis/mapper/basic";
    private static final String servicePath = servicePathRoot + "/" + "com/icbc/cfbi/wecom/online/standard";
    private static final String controllerPath = servicePathRoot + "/" + "com/icbc/cfbi/wecom/online/controller";
    private static final String enumPackage = "com.icbc.cfbi.wecom.common.enums";
    */
    private static final String commonPathRoot = projectPath + "/" + "src/main/java";
    private static final String enumPath = commonPathRoot + "/" + "tech/wedev/wecom/enums";
    private static final String servicePathClassRoot = projectPath;
    private static final String servicePathRoot = servicePathClassRoot + "/" + "src/main/java";
    private static final String poPath = commonPathRoot + "/" + "tech/wedev/wecom/entity/po";
    private static final String qoPath = commonPathRoot + "/" + "tech/wedev/wecom/entity/qo";
    private static final String mapperPath = servicePathRoot + "/" + "tech/wedev/wecom/dao";
    private static final String mapperXmlPath = servicePathClassRoot + "/" + "src/main/resources/mybatis/mapper";
    private static final String servicePath = servicePathRoot + "/" + "tech/wedev/wecom/standard";
    private static final String controllerPath = servicePathRoot + "/" + "tech/wedev/wecom/controller";
    private static final String enumPackage = "tech.wedev.wecom.enums";
    private static final Set<String> enumTypes = new HashSet<>();
    private static final Set<String> poBaseFields = new HashSet<>();
    private static final String controllerPathPre = System.getProperty("wecom.tableprefix");

    static {
        jdbcToJavaTypeMap.put("bigint", "java.lang.Long");
        jdbcToJavaTypeMap.put("decimal", "java.math.BigDecimal");
        jdbcToJavaTypeMap.put("int", "java.lang.Integer");
        jdbcToJavaTypeMap.put("tinyint", "java.lang.Integer");
        jdbcToJavaTypeMap.put("varchar", "java.lang.String");
        jdbcToJavaTypeMap.put("char", "java.lang.String");
        jdbcToJavaTypeMap.put("text", "java.lang.String");
        jdbcToJavaTypeMap.put("datetime", "java.util.Date");
        jdbcToJavaTypeMap.put("date", "java.util.Date");
        jdbcToJavaTypeMap.put("longtext", "java.lang.String");
        enumTypes.add("Integer");
        enumTypes.add("String");
        poBaseFields.add("id");
        poBaseFields.add("isDeleted");
        poBaseFields.add("gmtCreate");
        poBaseFields.add("gmtModified");
        poBaseFields.add("createId");
        poBaseFields.add("modifiedId");
        ExceptionAssert.isTrue(StringUtils.isBlank(controllerPathPre), "wecom.tableprefix不能为空");
    }

    @SneakyThrows
    public static void main(String[] args) {
        String enums = System.getProperty("wecom.enums");
        String enumsDescs = System.getProperty("wecom.enums.descs");

        String beanFileName = System.getProperty("wecom.beanname");
        String tableName = System.getProperty("wecom.tablename");

        beanFileName = BeanUtils.defaultIfNull(beanFileName, StringUtils.capitalizeFirstLetter(StringUtils.columnNameToFieldName(tableName.substring(tableName.indexOf(controllerPathPre) + controllerPathPre.length() + 1))));

//        ExceptionAssert.isTrue(StringUtils.isBlank(beanFileName), "bean名不能为空");
        ExceptionAssert.isTrue(StringUtils.isBlank(tableName), "表名不能为空");
        Map<String, String> enumInfoDescMap = getEnumInfoDescMap(enums, enumsDescs);

//        DataSource ds = new SimpleDataSource();
//        加载配置文件
        Properties applicationProperties = new Properties();
        Properties activeapplicationProperties = new Properties();
        String propertyFileSuffix = StringUtils.defaultVal(System.getProperty("spring.profiles.active"), "");
        if (StringUtils.isNotBlank(propertyFileSuffix)) {
            String propertiesFile = "application-" + propertyFileSuffix + " properties";
            try (InputStream resourceAsStream = LowCodeUtils.class.getClassLoader().getResourceAsStream(propertiesFile)) {
                activeapplicationProperties.load(resourceAsStream);
            }
        }
        applicationProperties.load(LowCodeUtils.class.getClassLoader().getResourceAsStream("application.properties"));
        applicationProperties.putAll(activeapplicationProperties);
        //获取数据库字段信息
        List<Triplet<String, String, Class>> triplets = LowCodeUtils.getTriplets(tableName, applicationProperties);
        List<String> columnNames = triplets.stream().map(Triplet::getLeft).collect(Collectors.toList());

        Set<String> enumFields = enumInfoDescMap.keySet().stream().map(a -> a.substring(0, a.indexOf(":"))).collect(Collectors.toSet());
        List<Triplet<String, String, Class>> enumtriplets = new ArrayList<>();
        for (Iterator<Triplet<String, String, Class>> it = triplets.iterator(); it.hasNext(); ) {
            Triplet<String, String, Class> triplet = it.next();
            String field = StringUtils.columnNameToFieldName(triplet.getLeft());
            if (enumFields.contains(field)) {
                enumtriplets.add(triplet);
                enumFields.remove(field);
                it.remove();
            }
        }
        ExceptionAssert.isFalse(enumFields.isEmpty(), CollectionUtils.join(enumFields.stream().collect(Collectors.toList()), ",") + "等字段，在表中不存在");
        String poPackage = org.apache.commons.lang3.StringUtils.replace(poPath.substring(commonPathRoot.length()), "/", ".").substring(1);
        //PO
        StringBuilder poSb = new StringBuilder();
        StringBuilder qoSb = new StringBuilder();
        poSb.append("package").append(blankChar).append(poPackage).append(";").append(System.getProperty("line.separator"));
        qoSb.append("package").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(qoPath.substring(commonPathRoot.length()), "/", ".").substring(1)).append(";").append(System.getProperty("line.separator"));
        Set<String> basePOFieldSet = Arrays.stream(BasicPO.class.getDeclaredFields()).map(a -> a.getName()).collect(Collectors.toSet());

        for (Triplet<String, String, Class> triplet : enumtriplets) {
            String fieldName = StringUtils.columnNameToFieldName(triplet.getLeft());
            if (basePOFieldSet.contains(fieldName)) {
                continue;
            }

            String enumName = beanFileName + StringUtils.capitalizeFirstLetter(fieldName) + "Enum";
            poSb.append("import").append(blankChar).append(enumPackage).append(".").append(enumName).append(";").append(System.getProperty("line.separator"));
            qoSb.append("import").append(blankChar).append(enumPackage).append(".").append(enumName).append(";").append(System.getProperty("line.separator"));
        }
        poSb.append("import").append(blankChar).append("lombok.AllArgsConstructor").append(";").append(System.getProperty("line.separator"));
        poSb.append("import").append(blankChar).append("lombok.Data").append(";").append(System.getProperty("line.separator"));
        poSb.append("import").append(blankChar).append("lombok.NoArgsConstructor").append(";").append(System.getProperty("line.separator"));
        poSb.append("import").append(blankChar).append("lombok.experimental.SuperBuilder").append(";").append(System.getProperty("line.separator"));
//        poSb.append("import").append(blankChar).append("com.icbc.cfbi.wecom.common.annos.TableName").append(";").append(System.getProperty("line.separator"));
        poSb.append("import").append(blankChar).append("tech.wedev.wecom.annos.TableName").append(";").append(System.getProperty("line.separator"));
        qoSb.append("import").append(blankChar).append("lombok.AllArgsConstructor").append(";").append(System.getProperty("line.separator"));
        qoSb.append("import").append(blankChar).append("lombok.Data").append(";").append(System.getProperty("line.separator"));
        qoSb.append("import").append(blankChar).append("lombok.NoArgsConstructor").append(";").append(System.getProperty("line.separator"));
        qoSb.append("import").append(blankChar).append("lombok.experimental.SuperBuilder").append(";").append(System.getProperty("line.separator"));
        qoSb.append("import").append(blankChar).append("tech.wedev.wecom.annos.TableName").append(";").append(System.getProperty("line.separator"));
        Set<String> fieldClasses = triplets.stream().filter(a -> !basePOFieldSet.contains(a.getLeft())).map(a -> a.getRight().getName()).filter(a -> !a.startsWith("java.lang")).collect(Collectors.toSet());
        for (String fieldClass : fieldClasses) {
            poSb.append("import").append(blankChar).append(fieldClass).append(";").append(System.getProperty("line.separator"));
            qoSb.append("import").append(blankChar).append(fieldClass).append(";").append(System.getProperty("line.separator"));
        }
        poSb.append(System.getProperty("line.separator"));
        qoSb.append(System.getProperty("line.separator"));
        poSb.append(System.getProperty("line.separator"));
        qoSb.append(System.getProperty("line.separator"));

        poSb.append("@Data").append(System.getProperty("line.separator"));
        poSb.append("@SuperBuilder").append(System.getProperty("line.separator"));
        poSb.append("@NoArgsConstructor").append(System.getProperty("line.separator"));
        poSb.append("@AllArgsConstructor").append(System.getProperty("line.separator"));
        poSb.append("@TableName(\"").append(tableName).append("\")").append(System.getProperty("line.separator"));

        qoSb.append("@Data").append(System.getProperty("line.separator"));
        qoSb.append("@SuperBuilder").append(System.getProperty("line.separator"));
        qoSb.append("@NoArgsConstructor").append(System.getProperty("line.separator"));
        qoSb.append("@AllArgsConstructor").append(System.getProperty("line.separator"));
        qoSb.append("@TableName(\"").append(tableName).append("\")").append(System.getProperty("line.separator"));

        poSb.append("public class").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(blankChar).append("extends").append(blankChar).append("BasicPO").append(" {").append(System.getProperty("line.separator"));
        qoSb.append("public class").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(blankChar).append("extends").append(blankChar).append("BasicQO").append(" {").append(System.getProperty("line.separator"));
        for (Triplet<String, String, Class> triplet : triplets) {
            String fieldName = StringUtils.columnNameToFieldName(triplet.getLeft());
            if (basePOFieldSet.contains(fieldName)) {
                continue;
            }
            poSb.append(tabChar).append("/**").append(System.getProperty("line.separator")).append(tabChar).append("*").append(blankChar).append(triplet.getCenter()).append(System.getProperty("line.separator")).append(tabChar).append("*/").append(System.getProperty("line.separator"));
            poSb.append(tabChar).append("private").append(blankChar).append(triplet.getRight().getSimpleName()).append(blankChar).append(fieldName).append(";").append(System.getProperty("line.separator"));
            qoSb.append(tabChar).append("/**").append(System.getProperty("line.separator")).append(tabChar).append("*").append(blankChar).append(triplet.getCenter()).append(System.getProperty("line.separator")).append(tabChar).append("*/").append(System.getProperty("line.separator"));
            qoSb.append(tabChar).append("private").append(blankChar).append(triplet.getRight().getSimpleName()).append(blankChar).append(fieldName).append(";").append(System.getProperty("line.separator"));
        }

        for (Triplet<String, String, Class> triplet : enumtriplets) {
            String fieldName = StringUtils.columnNameToFieldName(triplet.getLeft());
            if (basePOFieldSet.contains(fieldName)) {
                continue;
            }
            String enumName = beanFileName + StringUtils.capitalizeFirstLetter(fieldName) + "Enum";
            enumInfoDescMap.keySet().stream().filter(a -> a.substring(0, a.indexOf(":")).equals(fieldName)).collect(Collectors.toSet());
            poSb.append(tabChar).append("/**").append(System.getProperty("line.separator")).append(tabChar).append("*").append(blankChar).append(triplet.getCenter()).append(System.getProperty("line.separator")).append(tabChar).append("*/").append(System.getProperty("line.separator"));
            poSb.append(tabChar).append("private").append(blankChar).append(enumName).append(blankChar).append(fieldName).append(";").append(System.getProperty("line.separator"));
            qoSb.append(tabChar).append("/**").append(System.getProperty("line.separator")).append(tabChar).append("*").append(blankChar).append(triplet.getCenter()).append(System.getProperty("line.separator")).append(tabChar).append("*/").append(System.getProperty("line.separator"));
            qoSb.append(tabChar).append("private").append(blankChar).append(enumName).append(blankChar).append(fieldName).append(";").append(System.getProperty("line.separator"));
        }
        poSb.append("}");
        qoSb.append("}");

        Map<String, String> enumFileInfo = getEnum(beanFileName, enumInfoDescMap);

        //mapperXml
        StringBuilder mapperXml = getMapperXml(beanFileName, columnNames, poPackage);

        StringBuilder mapper = getMapper(beanFileName, poPackage);

        //service
        StringBuilder service = getService(beanFileName, poPackage);

        StringBuilder serviceImpl = getServiceImpl(beanFileName, poPackage);

        //controller
        StringBuilder controller = getController(beanFileName, poPackage);

        Map<File, String> enumFiles = new HashMap<>();
        for (Map.Entry<String, String> enumName : enumFileInfo.entrySet()) {
            File enumFile = new File(enumPath + "/" + enumName.getKey() + ".java");
            ExceptionAssert.isTrue(enumFile.exists(), enumName.getKey() + "已存在");
            enumFiles.put(enumFile, enumName.getValue());
        }

        File poFile = new File(poPath + "/" + StringUtils.capitalizeFirstLetter(beanFileName) + "PO" + ".java");
        ExceptionAssert.isTrue(poFile.exists(), poFile.getName() + "已存在");
        File qoFile = new File(qoPath + "/" + StringUtils.capitalizeFirstLetter(beanFileName) + "QO" + ".java");
        ExceptionAssert.isTrue(qoFile.exists(), qoFile.getName() + "已存在");
        File mapperXmlFile = new File(mapperXmlPath + "/" + StringUtils.capitalizeFirstLetter(beanFileName) + "Mapper" + ".xml");
        ExceptionAssert.isTrue(mapperXmlFile.exists(), mapperXmlFile.getName() + "已存在");
        File mapperFile = new File(mapperPath + "/" + StringUtils.capitalizeFirstLetter(beanFileName) + "Mapper" + ".java");
        ExceptionAssert.isTrue(mapperFile.exists(), mapperFile.getName() + "已存在");
        File serviceFile = new File(servicePath + "/" + StringUtils.capitalizeFirstLetter(beanFileName) + "Service" + ".java");
        ExceptionAssert.isTrue(serviceFile.exists(), serviceFile.getName() + "已存在");
        File serviceImplFile = new File(servicePath + "/impl/" + StringUtils.capitalizeFirstLetter(beanFileName) + "ServiceImpl" + ".java");
        ExceptionAssert.isTrue(serviceImplFile.exists(), serviceImplFile.getName() + "已存在");
        File controllerFile = new File(controllerPath + "/" + StringUtils.capitalizeFirstLetter(beanFileName) + "Controller" + ".java");
        ExceptionAssert.isTrue(controllerFile.exists(), controllerFile.getName() + "已存在");

        FileUtils.write(poFile, poSb.toString(), "UTF-8");
        FileUtils.write(qoFile, qoSb.toString(), "UTF-8");
        FileUtils.write(mapperXmlFile, mapperXml.toString(), "UTF-8");
        FileUtils.write(mapperFile, mapper.toString(), "UTF-8");
        FileUtils.write(serviceFile, service.toString(), "UTF-8");
        FileUtils.write(serviceImplFile, serviceImpl.toString(), "UTF-8");
        FileUtils.write(controllerFile, controller.toString(), "UTF-8");
        for (Map.Entry<File, String> fileStringEntry : enumFiles.entrySet()) {
            FileUtils.write(fileStringEntry.getKey(), fileStringEntry.getValue(), "UTF-8");
        }
    }

    private static Map<String, String> getEnumInfoDescMap(String enums, String enumsDescs) {
        Map<String, String> enumInfoDescMap = new HashMap<>();
        if (StringUtils.isNotBlank(enums)) {
            String[] split = enums.split("-");
            List<String> enumFields = ArrayUtils.asArrayList(split);
            for (String enumField : enumFields) {
                ExceptionAssert.isFalse(StringUtils.isMatch("a:Integer", "[A-Za-z0-9]+:(Integer|String){1}"), enumField + "配置不正确，类型只能为Integer或String，字段名称只能是字母数字");
            }
            ExceptionAssert.isTrue(StringUtils.isBlank(enumsDescs), "枚举描述不能为空");
            String[] split1 = enumsDescs.split("-");
            List<String> enumsFieldsDescs = ArrayUtils.asArrayList(split1);
            ExceptionAssert.isTrue(enumFields.size() != enumsFieldsDescs.size(), "枚举字段数量和描述教量不一致");
            for (int i = 0; i < split.length; i++) {
                enumInfoDescMap.put(split[i], split1[i]);
            }
        }
        return enumInfoDescMap;
    }

    private static Map<String, String> getEnum(String beanFileName, Map<String, String> enumInfoDescMap) {
        Map<String, String> enumFileInfo = new HashMap<>();
        for (Map.Entry<String, String> enumInfo : enumInfoDescMap.entrySet()) {
            String fieldAndType = enumInfo.getKey();
            String valueInfo = enumInfo.getValue();
            StringBuilder sb = new StringBuilder();
            String[] split = fieldAndType.split(":");
            String field = split[0];
            String type = split[1];
            String enumName = beanFileName + StringUtils.capitalizeFirstLetter(field) + "Enum";
            sb.append("package ").append(enumPackage).append(";").append(System.getProperty("line.separator"));
            sb.append("public enum ").append(enumName).append(" implements ");
            String[] fieldInfos = valueInfo.split("[,，]");
            if (type.equals("Integer")) {
                sb.append("BaseIntegerEnum ").append("{").append(System.getProperty("line.separator"));
                for (int i = 0; i < fieldInfos.length; i++) {
                    String fieldInfo = fieldInfos[i];
                    String[] split1 = fieldInfo.split(":");
                    sb.append(tabChar).append(StringUtils.fieldNameToColumnName(split1[1]).toUpperCase()).append("(").append(split1[0]).append(",\"").append(split1[2]).append("\")");
                    if (i == fieldInfos.length - 1) {
                        sb.append(";").append(System.getProperty("line.separator"));
                    } else {
                        sb.append(",").append(System.getProperty("line.separator"));
                    }

                }
            } else if (type.equals("String")) {
                sb.append("BaseStringEnum ").append("{").append(System.getProperty("line.separator"));
                for (int i = 0; i < fieldInfos.length; i++) {
                    String fieldInfo = fieldInfos[i];
                    String[] split1 = fieldInfo.split(":");
                    sb.append(tabChar).append(StringUtils.fieldNameToColumnName(split1[1]).toUpperCase()).append("(\"").append(split1[0]).append("\",\"").append(split1[2]).append("\")");
                    if (i == fieldInfos.length - 1) {
                        sb.append(";").append(System.getProperty("line.separator"));
                    } else {
                        sb.append(",").append(System.getProperty("line.separator"));
                    }
                }
            }
            if (type.equals("Integer")) {
                sb.append(tabChar).append("private final Integer code;").append(System.getProperty("line.separator"));
            } else if (type.equals("String")) {
                sb.append(tabChar).append("private final String code;").append(System.getProperty("line.separator"));
            }
            sb.append(tabChar).append("private final String desc;").append(System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));
            if (type.equals("Integer")) {
                sb.append(tabChar).append(enumName).append(" (Integer code, String desc) {").append(System.getProperty("line.separator"));
            } else if (type.equals("String")) {
                sb.append(tabChar).append(enumName).append("(String code, String desc) {").append(System.getProperty("line.separator"));
            }
            sb.append(tabChar).append(tabChar).append("this.code=code; ").append(System.getProperty("line.separator"));
            sb.append(tabChar).append(tabChar).append("this.desc=desc;").append(System.getProperty("line.separator"));
            sb.append(tabChar).append("}").append(System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));
            sb.append(tabChar).append("@Override").append(System.getProperty("line.separator"));
            if (type.equals("Integer")) {
                sb.append(tabChar).append("public Integer getCode() {").append(System.getProperty("line.separator"));
            } else if (type.equals("String")) {
                sb.append(tabChar).append("public String getCode() {").append(System.getProperty("line.separator"));
            }
            sb.append(tabChar).append(tabChar).append("return code;").append(System.getProperty("line.separator"));
            sb.append(tabChar).append("}").append(System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));

            sb.append(tabChar).append("@Override").append(System.getProperty("line.separator"));
            sb.append(tabChar).append("public String getDesc() {").append(System.getProperty("line.separator"));
            sb.append(tabChar).append(tabChar).append("return desc;").append(System.getProperty("line.separator"));
            sb.append(tabChar).append("}").append(System.getProperty("line.separator"));
            sb.append("}");
            enumFileInfo.put(enumName, sb.toString());
        }
        return enumFileInfo;
    }

    private static StringBuilder getService(String beanFileName, String poPackage) {
        StringBuilder service = new StringBuilder();
        service.append("package").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(servicePath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(";").append(System.getProperty("line.separator"));
//        service.append("import").append(blankChar).append("com.jsh.erp.BaseService").append(";").append(System.getProperty("line.separator"));
        service.append("import").append(blankChar).append(poPackage).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(";").append(System.getProperty("line.separator"));
        service.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(qoPath.substring(commonPathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(";").append(System.getProperty("line.separator"));
        service.append(System.getProperty("line.separator"));
        service.append(System.getProperty("line.separator"));

        service.append("public").append(blankChar).append("interface").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Service").append(blankChar).append("extends").append(blankChar).append("BasicService<").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(",").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(">").append(blankChar).append("{").append(System.getProperty("line.separator"));
        service.append(System.getProperty("line.separator"));
        service.append("}");
        return service;
    }

    private static StringBuilder getMapper(String beanFileName, String poPackage) {
        StringBuilder mapper = new StringBuilder();
        mapper.append("package").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(mapperPath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(";").append(System.getProperty("line.separator"));
        mapper.append("import").append(blankChar).append(poPackage).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(";").append(System.getProperty("line.separator"));
        mapper.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(qoPath.substring(commonPathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(";").append(System.getProperty("line.separator"));
        mapper.append("import tech.wedev.wecom.dao.BasicMapper;").append(System.getProperty("line.separator"));
        mapper.append(System.getProperty("line.separator"));
        mapper.append(System.getProperty("line.separator"));
        mapper.append("public").append(blankChar).append("interface").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Mapper").append(blankChar).append("extends").append(blankChar).append("BasicMapper<").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(",").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(">").append(blankChar).append("{").append(System.getProperty("line.separator"));
        mapper.append(System.getProperty("line.separator"));
        mapper.append("}");
        return mapper;
    }

    private static StringBuilder getMapperXml(String beanFileName, List<String> columnNames, String poPackage) {
        StringBuilder mapperXml = new StringBuilder();
        mapperXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(System.getProperty("line.separator"));
        mapperXml.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">").append(System.getProperty("line.separator"));
        mapperXml.append("<mapper namespace=\"").append(org.apache.commons.lang3.StringUtils.replace(mapperPath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Mapper").append("\">").append(System.getProperty("line.separator"));
        mapperXml.append(xmlBlankChar).append("<resultMap ").append("id=\"BaseResultMap\" ").append("type=\"").append(poPackage).append(".").append(beanFileName).append("PO").append("\">").append(System.getProperty("line.separator"));
        mapperXml.append(xmlBlankChar).append(xmlBlankChar).append("<id column=\"id\" property=\"id\" jdbcType=\"BIGINT\"/>").append(System.getProperty("line.separator"));
        for (String columnName : columnNames) {
            if ("id".equals(columnName)) {
                continue;
            }
            mapperXml.append(xmlBlankChar).append(xmlBlankChar).append("<result column=\"").append(columnName).append("\" ").append("property=\"").append(StringUtils.columnNameToFieldName(columnName)).append("\"/>").append(System.getProperty("line.separator"));
        }
        mapperXml.append(xmlBlankChar).append("</resultMap>").append(System.getProperty("line.separator"));

//列
        mapperXml.append(xmlBlankChar).append(" <sql id=\"Base_Column_List\">").append(System.getProperty("line.separator"));
        mapperXml.append(xmlBlankChar).append(xmlBlankChar);
        mapperXml.append(xmlBlankChar).append(StringUtils.join(columnNames.stream().map(a -> "`" + a + "`").collect(Collectors.toList()), ",")).append(System.getProperty("line.separator"));
        mapperXml.append(xmlBlankChar).append(" </sql>").append(System.getProperty("line.separator"));

        mapperXml.append("</mapper>").append(System.getProperty("line.separator"));
        return mapperXml;
    }

    private static StringBuilder getServiceImpl(String beanFileName, String poPackage) {
        StringBuilder serviceImpl = new StringBuilder();
        serviceImpl.append("package").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(servicePath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append("impl").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(servicePath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Service").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append("import").append(blankChar).append(poPackage).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(qoPath.substring(commonPathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(";").append(System.getProperty("line.separator"));
//        serviceImpl.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(mapperPath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append("BasicMapper").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append("import tech.wedev.wecom.dao.BasicMapper;").append(System.getProperty("line.separator"));
        serviceImpl.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(mapperPath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Mapper").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append("import").append(blankChar).append("org.springframework.beans.factory.annotation.Autowired").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append("import").append(blankChar).append("org.springframework.stereotype.Service").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append(System.getProperty("line.separator"));
        serviceImpl.append(System.getProperty("line.separator"));
        serviceImpl.append("@Service").append(System.getProperty("line.separator"));
        serviceImpl.append("public").append(blankChar).append("class").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("ServiceImpl").append(blankChar).append("extends").append(blankChar).append("BasicServiceImpl<").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(",").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(">").append(blankChar).append("implements").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Service").append(blankChar).append("{").append(System.getProperty("line.separator"));
        serviceImpl.append(System.getProperty("line.separator"));
        serviceImpl.append(tabChar).append("@Autowired").append(System.getProperty("line.separator"));
        serviceImpl.append(tabChar).append("private").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Mapper").append(blankChar).append(StringUtils.lowerCaseFirstLetter(beanFileName)).append("Mapper").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append(tabChar).append("@Override").append(System.getProperty("line.separator"));
        serviceImpl.append(tabChar).append("public").append(blankChar).append("BasicMapper<").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(",").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(">").append(blankChar).append("getBasicMapper()").append("{").append(System.getProperty("line.separator"));
        serviceImpl.append(tabChar).append(tabChar).append("return").append(blankChar).append(StringUtils.lowerCaseFirstLetter(beanFileName)).append("Mapper").append(";").append(System.getProperty("line.separator"));
        serviceImpl.append(tabChar).append("}").append(System.getProperty("line.separator"));
        serviceImpl.append("}");
        return serviceImpl;
    }

    private static StringBuilder getController(String beanFileName, String poPackage) {
        StringBuilder controller = new StringBuilder();
        controller.append("package").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(controllerPath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append("org.springframework.beans.factory.annotation.Autowired").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append("org.springframework.web.bind.annotation.RequestMapping").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append("org.springframework.web.bind.annotation.RestController").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append("lombok.extern.slf4j.Slf4j").append(";").append(System.getProperty("line.separator"));
//        controller.append("import").append(blankChar).append("com.icbc.cfbi.wecom.online.controller.BasicController").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(servicePath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Service").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(servicePath.substring(servicePathRoot.length()), "/", ".").substring(1)).append(".").append("BasicService").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append(poPackage).append(" . ").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(";").append(System.getProperty("line.separator"));
        controller.append("import").append(blankChar).append(org.apache.commons.lang3.StringUtils.replace(qoPath.substring(commonPathRoot.length()), "/", ".").substring(1)).append(".").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(";").append(System.getProperty("line.separator"));
        controller.append(System.getProperty("line.separator"));
        controller.append(System.getProperty("line.separator"));
        controller.append("@RestController").append(System.getProperty("line.separator"));
        controller.append("@RequestMapping").append("(").append("\"").append(controllerPathPre).append(StringUtils.fieldNameToColumnName(beanFileName)).append("\"").append(")").append(System.getProperty("line.separator"));
        controller.append("@Slf4j").append(System.getProperty("line.separator"));
        controller.append("public").append(blankChar).append("class").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Controller").append(blankChar).append("extends").append(blankChar).append("BasicController<").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(", ").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(">").append(blankChar).append("{").append(System.getProperty("line.separator"));
        controller.append(tabChar).append("@Autowired").append(System.getProperty("line.separator"));
        controller.append(tabChar).append("private").append(blankChar).append(StringUtils.capitalizeFirstLetter(beanFileName)).append("Service").append(blankChar).append(StringUtils.lowerCaseFirstLetter(beanFileName)).append("Service").append(";").append(System.getProperty("line.separator"));
        controller.append(tabChar).append("@Override").append(System.getProperty("line.separator"));
        controller.append(tabChar).append("public").append(blankChar).append("BasicService<").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("PO").append(",").append(StringUtils.capitalizeFirstLetter(beanFileName)).append("QO").append(">").append(blankChar).append("getBasicService()").append("{").append(System.getProperty("line.separator"));
        controller.append(tabChar).append(tabChar).append("return").append(blankChar).append(StringUtils.lowerCaseFirstLetter(beanFileName)).append("Service").append(";").append(System.getProperty("line.separator"));
        controller.append(tabChar).append("}").append(System.getProperty("line.separator"));
        controller.append("}");
        return controller;
    }

    private static List<Triplet<String, String, Class>> getTriplets(String tableName, Properties applicationProperties) throws ClassNotFoundException, SQLException {
        List<Triplet<String, String, Class>> triplets = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String password = SM4Util.decryptEcb(SM4Util.hexKey, applicationProperties.getProperty("spring.datasource.password"));
            String url = applicationProperties.getProperty("spring.datasource.url");
            String tableSchema = StringUtils.matchOne(url, "(.*\\?)").replaceAll("(.*\\/)", "").replace("?", "");
            Connection connection = DriverManager.getConnection(url, applicationProperties.getProperty("spring.datasource.username"), password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM information_schema.columns WHERE table_schema= ? AND table_name=?;");
            preparedStatement.setString(1, tableSchema);
            preparedStatement.setString(2, tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String field = resultSet.getString(4);
                String jdbcType = resultSet.getString(8);
                String fieldClass = jdbcToJavaTypeMap.get(jdbcType);
                String comment = resultSet.getString(20);
//        ExceptionAssert.isTrue(StringUtils.isBlank(comment), field + "字段的注释在数据库中为空");
                if (fieldClass == null) {
                    throw new RuntimeException("没有找到对应的JAVA类型");
                }
                triplets.add(Triplet.<String, String, Class>builder().left(field).center(comment).right(Class.forName(fieldClass)).build());
            }
        } finally {

        }
        return triplets;
    }
}