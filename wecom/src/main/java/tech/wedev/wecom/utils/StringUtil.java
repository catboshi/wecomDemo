package tech.wedev.wecom.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public final class StringUtil {
    private StringUtil() {

    }

    public static List<String> split(String src, String spliter) {
        if (isBlank(src)) {
            return new ArrayList<>();
        }
        return Arrays.asList(src.split(spliter));
    }

    public static String capitalizeFirstLetter(String src) {
        if (isBlank(src)) {
            return src;
        }
        return src.substring(0, 1).toUpperCase() + src.substring(1);
    }

    public static String lowerCaseFirstLetter(String src) {
        if (isBlank(src)) {
            return src;
        }
        return src.substring(0, 1).toLowerCase() + src.substring(1);
    }

    public static String subString(String val, int start, int end) {
        if (start > end) {
            throw new RuntimeException("start不能比end大");
        }

        val = defaultVal(val, "");
        int length = val.length();
        if (length < end) {
            return val.substring(start, length);
        }
        return val.substring(start, end);
    }

    public static boolean isBlank(String val) {
        if (val == null || val.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String val) {
        return !isBlank(val);
    }

    public static String lefPad(String str, int size, String padChar) {
        str = StringUtil.defaultVal(str, "");
        if (str.length() >= size) {
            return str;
        }
        final int padNum = size - str.length();
        String pad = "";
        for (int i = 0; i < padNum; i++) {
            pad += padChar;
        }
        return pad + str;
    }

    public static String defaultVal(String val, String def) {
        if (isBlank(val)) {
            return def;
        }
        return val;
    }

    public static String format(String src, Object... args) {
        return format(src, true, args);
    }

    public static String format(String src, boolean isSplit, Object... args) {
        src = defaultVal(src, "");
        String flag = "{}";
        if (args == null) {
            return src;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                s.add("null");
            } else {
                s.add(args[i].toString());
            }
        }

        for (int i = 0; src.indexOf(flag) != -1 && i < s.size(); i++) {
            final int ind = src.indexOf(flag);
            src = src.substring(0, ind) + (isSplit ? ":" : "") + s.get(i) + "," + src.substring(ind + flag.length());
        }

        if (src.length() == src.lastIndexOf(",") + 1) {
            src = src.substring(0, src.length() - 1);
        }
        return src.replace(flag, "");
    }

    public static <T> String join(String spliter,T ... ts) { return join(Arrays.asList(ts) , spliter);}
    public static <T> String join(Function<T, String> sonProcessor, String spliter,T ... ts) {
        return join(Arrays.asList(ts) , spliter, sonProcessor);
    }

    public static <T> String join(List<T> lists, String spliter, Function<T, String> sonProcessor) {
        StringBuilder sb = new StringBuilder() ;
        for (int i = 0; i < lists.size(); i++) {
            sb.append(sonProcessor.apply(lists.get(i))).append(i == (lists.size() - 1) ? "" : spliter);
        }
        return sb.toString();
    }

    public static <T> String join(List<T> lists, String spliter) {
        StringBuilder sb = new StringBuilder() ;
        for (int i = 0; i < lists.size(); i++) {
            sb.append(lists.get (i)) .append (i == (lists.size() - 1) ? "" : spliter);
        }
        return sb.toString();
    }

    public static <T> List<String> match(String src, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(src);
        List<String> array = new ArrayList<>();
        while (m.find()) {
            array.add(m.group());
        }
        return array;
    }
    public static <T> String matchOne(String src, String regex) {
        final List<String> match = match(src, regex);
        RuntimeExceptionUtil.isTrue(match.size() > 1, format("结果集大于1, src{}regex{}", src, regex));
        if (match.size() == 1) {
            return match.get(0);
        }
        return "";
    }

    public static boolean isMatch(String src, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(src);
        return m.matches ();
    }

    public static boolean isNotMatch(String src, String regex) { return !isMatch(src, regex);}

    public static String fieldNameToColumnName(String beanFieldName){
        StringBuilder stringBuilder = new StringBuilder();
        Pattern p = Pattern.compile("[A-Z]{1}");
        a://什么意思？
        for (int i = 0; i < beanFieldName.length(); i++) {
            String substring = beanFieldName.substring(i, i + 1);
            Matcher m = p.matcher(substring);
            while (m.find()) {
                String group = m.group();
                stringBuilder.append("_" + group.toLowerCase());
                continue a;
            }
            stringBuilder.append(substring);
        }
        return stringBuilder.toString();
    }

    public static boolean endsWith(String src, String... suffixes) {
        for (String suffix : suffixes){
            if(src.endsWith(suffix)){
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty (String str) {return str == null || "".equals(str.trim());}

    public static boolean isNotEmpty (String str) {return !isEmpty(str);}

    //列名转bean字段名
    public static String columnNameToFieldName (String columnName) {
        Pattern p = Pattern.compile("\\_[a-z]{1}");
        Matcher m = p.matcher(columnName);
        while (m.find()) {
            String group = m.group();
            columnName = columnName.replaceAll(group, group.toUpperCase().substring(1)) ;
        }
        return columnName;
    }
}

