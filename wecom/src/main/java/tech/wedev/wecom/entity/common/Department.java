package tech.wedev.wecom.entity.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Department implements Serializable {

    private String dept_certId;
    //部门代码
    private String dept_code;
    //父部门代码
    private String dept_upCode;
    //父部门代码路径
    private String dept_upCodePath;
    //部门名称
    private String dept_name;
    //部门英文名称
    private String dept_name_en;
    //部门路径
    private String dept_path;
    //部门英文路径
    private String dept_path_en;
    //部门代码路径
    private String dept_codePath;
    //部门主管
    private String dept_manager;
    //部门级别
    private int dept_level;
    //部门序列
    private String dept_seq;
    //所属国家
    private String dept_base_en;

}
