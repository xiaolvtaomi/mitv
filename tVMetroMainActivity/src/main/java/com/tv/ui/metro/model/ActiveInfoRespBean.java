package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class ActiveInfoRespBean implements Serializable {
    String code ;
    String msg ;
    Map<String, List<ActiveInfoBean>> data ;

    public Map<String, List<ActiveInfoBean>> getData() {
        return data;
    }

    public void setData(Map<String, List<ActiveInfoBean>> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
