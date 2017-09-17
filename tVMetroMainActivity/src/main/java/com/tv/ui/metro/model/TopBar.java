package com.tv.ui.metro.model;

import java.io.Serializable;

/**
 * Created by lml on 17/9/17.
 */

public class TopBar implements Serializable {
    private static final long serialVersionUID = 1L;
    public String logo;
    public String title;
    public String bg;
    public String welcomebg;


    public TopBar clone() {
        TopBar item = new TopBar();
        item.logo = this.logo;
        item.title = this.title;
        item.bg = this.bg;
        item.welcomebg = this.welcomebg;
        return item;
    }

    public String toString() {
        return " logo:" + logo + " title:" + title + " bg:" + bg + " " +
                "welcomebg:" + welcomebg;
    }
}