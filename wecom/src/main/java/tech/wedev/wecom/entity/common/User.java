package tech.wedev.wecom.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User implements Serializable {
    //中文名
    private String notesname = "";
    //拼音
    private String username = "";
    //员工号
    private String userid = "";
    //用户内网邮箱
    private String userinmail = "";
    //部门路径
    private String deptpath = "";
    //部门路径code
    private String deptcodepath = "";
    //部门code
    private String deptcode = "";
    //部门名称
    private String deptname = "";
    //部门主管
    private String deptmanager = "";
    //部门主管code
    private String deptmanagercode = "";
    //是否离职：1-在职；0-离职
    private String userstatus = "";
    //上级主管
    private String superiormanager = "";
    //0-正式员工；1-外协
    private String usertype = "0";

}
