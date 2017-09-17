package com.tv.ui.metro.model;

import java.io.Serializable;

public class DisplayItem implements Serializable, Comparable<DisplayItem> {
    private static final long serialVersionUID = 5L;

    public static class UI implements Serializable {
        private static final long serialVersionUID = 1L;

        public static class Layout implements Serializable {
            private static final long serialVersionUID = 1L;

            public int x;
            public int y;
            public int w;
            public int h;

            public String type; // default U1x1
            public static final String U2x1 = "2x1";// 2 W cell, 1 H cell
            // public static final String U2x2 = "2x2";//2x2
            public static final String U1x1 = "1x1";
            public static final String U1x2 = "1x2";

            public Layout(){

            }

            public Layout(int x, int y, int w, int h){
                this.x = x ;
                this.y = y ;
                this.w = w;
                this.h = h ;
            }

            public Layout clone() {
                Layout item = new Layout();
                item.x = x;
                item.y = y;
                item.w = w;
                item.h = h;
                item.type = type;
                return item;
            }

            public String toString() {
                return "x: " + x + " y:" + y + " w:" + w + " h:" + h + " type:" + type;
            }

        }

        public static final String METRO_CELL_BANNER = "metro_cell_banner";
        public String type;
        public Layout layout;

        public UI clone() {
            UI item = new UI();
            item.type = type;
            if (layout != null) item.layout = layout.clone();
            return item;
        }

        public String toString() {
            return " type:" + type + "  layout:" + layout;
        }
    }

    public static class Times implements Serializable {
        private static final long serialVersionUID = 1L;
        public long updated;
        public long created;

        public Times clone() {
            Times item = new Times();
            item.created = created;
            item.updated = updated;
            return item;
        }
    }

    public static class Target implements Serializable {
        private static final long serialVersionUID = 1L;
        public String type;
        public String param;
        public String param_standby;
        public String jump_uri;
        public String download_url;
        public String download_url_phone;
        public String app_size;
        public String app_type;
        public String app_price;
        public String app_content;
        public String app_icon;
        public String app_pic;
        public String install_url;
        public String app_name;
        public String app_versions;



        public Target clone() {
            Target item = new Target();
            item.type = type;
            item.param = param;
            item.jump_uri=jump_uri;
            item.param_standby=param_standby;
            item.download_url=download_url;
            item.download_url_phone=download_url_phone;
            item.app_size=app_size;
            item.app_type=app_type;
            item.app_price=app_price;
            item.app_content=app_content;
            item.app_icon=app_icon;
            item.app_pic=app_pic;
            item.install_url=install_url;
            item.app_name=app_name;
            item.app_versions=app_versions;
            return item;
        }


    }

    public Target target;
    public String ns;
    public String type;
    public String id;
    public String name;
    public String focusable;
    public ImageGroup images;
    public UI _ui;
    public Times times;
    public int wc;
    public int hc;
    public String panelbg;
    public DisplayItem clone() {
        DisplayItem item = new DisplayItem();

        if (target != null) item.target = target.clone();

        item.ns = this.ns;
        item.type = this.type;
        item.id = this.id;
        item.name = this.name;
        item.wc=this.wc;
        item.hc=this.hc;
        item.panelbg=this.panelbg;
        item.focusable=this.focusable;
        if (images != null) item.images = this.images.clone();
        if (_ui != null) item._ui = this._ui.clone();
        if (times != null) item.times = times.clone();

        return item;
    }

    public String toString() {
        return " ns:" + ns + " type:" + type + " target=" + target + " id:" + id + " name:" + name + "images:"
                + images + " _ui:" + _ui+ " wc:" + wc+ " hc:" + hc+ " panelbg:" + panelbg+ " focusable:" + focusable;
    }

    @Override
    public int compareTo(DisplayItem another) {
        if (_ui == null || another._ui == null || _ui.layout == null || another._ui.layout == null) {
            return 0;
        }
        if (_ui.layout.x > another._ui.layout.x) {
            return 1;
        } else if (_ui.layout.x < another._ui.layout.x) {
            return -1;
        } else {
            return _ui.layout.y - another._ui.layout.y;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj instanceof DisplayItem){
            return  ((DisplayItem)obj).id.equals(id);
        }
        return  false;
    }
}