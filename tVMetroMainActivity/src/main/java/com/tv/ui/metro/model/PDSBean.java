package com.tv.ui.metro.model;

import java.io.Serializable;

/**
 * Created by lml on 17/9/17.
 */

public class PDSBean implements Serializable {
//    select ParentCode, code, Company,CalledNum, CalledPassword, CallingNum, CallingPassword from t_Directory;
    public String ParentCode ;
    public String code ;
    public String CalledNum ;
    public String CalledPassword ;
    public String CallingNum ;
    public String CallingPassword ;
    public String Company ;
    public String Category ;
    public boolean haschild ;

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getParentCode() {
        return ParentCode;
    }

    public void setParentCode(String parentCode) {
        ParentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCalledNum() {
        return CalledNum;
    }

    public void setCalledNum(String calledNum) {
        CalledNum = calledNum;
    }

    public String getCalledPassword() {
        return CalledPassword;
    }

    public void setCalledPassword(String calledPassword) {
        CalledPassword = calledPassword;
    }

    public String getCallingNum() {
        return CallingNum;
    }

    public void setCallingNum(String callingNum) {
        CallingNum = callingNum;
    }

    public String getCallingPassword() {
        return CallingPassword;
    }

    public void setCallingPassword(String callingPassword) {
        CallingPassword = callingPassword;
    }

}
