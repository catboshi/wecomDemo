package tech.wedev.wecom.utils;

import com.alibaba.excel.util.StringUtils;
import tech.wedev.wecom.entity.common.Department;

public class BaseUtil {
    public static String YES_NUM = "1";
    public static String NO_NUM = "0";
    public static String YES = "是";
    public static String NO = "否";

    /**
     * 把1与0转换成是与否
     */
    public static String getYesOrNoStr(Object num) {
        if (num == null) {
            return null;
        }
        if (YES_NUM.equals(num)) {
            return YES;
        }
        if (NO_NUM.equals(num)) {
            return NO;
        }
        return num.toString();
    }

    /**
     * 移除最后一级路径
     */
    public static String removeLastDeptCodePath(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        int lastIdx = path.lastIndexOf("\\");
        if (lastIdx == -1) {
            return null;
        }
        return path.substring(0, lastIdx);
    }

    /**
     * 根据部门全路径 获取前n级路径 以下为示例
     * path = ("015663\015664\015934");
     * getDeptCodePath(path, 1) = 015663;
     * getDeptCodePath(path, 2) = 015663\015664;
     * getDeptCodePath(path, 4) = "";
     * @param path 部门编码路径
     * @param n 获取前几级
     * @return 前n级部门编码
     */
    public static String getDeptCodePath(String path, int n) {
        if (n <= 0 || StringUtils.isEmpty(path)) {
            return "";
        }
        String[] pathArr = path.split("\\\\");

        if (pathArr.length < n) {
            return null;
        }
        StringBuilder returnSb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                returnSb.append('\\');
            }
            returnSb.append(pathArr[i]);
        }
        return returnSb.toString();
    }

    /**
     * 根据部门全路径 获取前n级路径 以下为示例
     * path = ("015663\015664\015934");
     * getWhichDeptCodePath(path, 1) = 015663;
     * getWhichDeptCodePath(path, 2) = 015663\015664;
     * getWhichDeptCodePath(path, 4) = 015663\015664\015934;
     * @param path 部门编码路径
     * @param which 获取第几级
     * @return 第which级部门编码
     */
    public static String getWhichDeptCodePath(String path, int which) {
        if (which <= 0 || StringUtils.isEmpty(path)) {
            return "";
        }
        String[] pathArr = path.split("\\\\");

        if (pathArr.length < which) {
            return "";
        }
        return pathArr[which - 1];
    }

    /**
     * 根据部门全路径 获取第which级路径 以下为示例
     * path = ("015663\015664\015934");
     * getDeptCodePathNonNull(path, 1) = 015663;
     * getDeptCodePathNonNull(path, 2) = 015663\015664;
     * getDeptCodePathNonNull(path, 4) = 015663\015664\015934;
     * @param path 部门编码路径
     * @param n 获取前几级
     * @return 前n级部门编码
     */
    public static String getDeptCodePathNonNull(String path, int n) {
        if (n <= 0 || StringUtils.isEmpty(path)) {
            return "";
        }
        String[] pathArr = path.split("\\\\");

        StringBuilder returnSb = new StringBuilder();
        for (int i = 0; i < n && i < pathArr.length; i++) {
            if (i != 0) {
                returnSb.append('\\');
            }
            returnSb.append(pathArr[i]);
        }
        return returnSb.toString();
    }

    /**
     * 根据部门全路径 获取最后一级路径 以下为示例
     * path = ("015663\015664\015934");
     * getLastDeptCodePath(path) = 015934;
     * @param path 部门编码路径
     */
    public static String getLastDeptCodePath(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        String[] pathArr = path.split("\\\\");

        if (pathArr.length <= 0) {
            return "";
        }
        return pathArr[pathArr.length-1];
    }

    /**
     * 获取完整的部门路径
     * getFullDeptCode("015934") = 015663\015664\015934;
     * @param deptCode 部门编码路径
     */
    public static String getFullDeptCode(String deptCode) {
        if (StringUtils.isEmpty(deptCode)) {
            return deptCode;
        }
        Department department = SpringRedisUtil.getOneDepartmentObj(deptCode);
        if (department == null) {
            return deptCode;
        }
        return department.getDept_codePath();
    }
}
