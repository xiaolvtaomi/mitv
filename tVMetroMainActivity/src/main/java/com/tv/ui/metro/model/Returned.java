package com.tv.ui.metro.model;

import com.tv.ui.metro.utils.GsonUtils;

/**
 * Created by Lenovo on 2017/6/5.
 */
public class Returned {
    private String  code;

    @Override
    public String toString() {
        return "Returned{" +
                "code='" + code + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Returned(String code) {

        this.code = code;
    }

    public static Returned parseData(String json) {
        return  GsonUtils.toBean(json, Returned.class);

    }
}
