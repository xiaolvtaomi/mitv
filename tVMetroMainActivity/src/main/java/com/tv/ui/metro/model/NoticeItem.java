package com.tv.ui.metro.model;

import java.io.Serializable;

public class NoticeItem implements Serializable{
    private static final long serialVersionUID = 5L;
    public String  backimg;
    public String  content;
    public String  id;
    public String  title;
    public String  url;
    public NoticeItem clone() {
        NoticeItem item = new NoticeItem();
        item.backimg = this.backimg;
        item.content = this.content;
        item.id = this.id;
        item.title = this.title;
        item.url=this.url;
        return item;
    }

    public String toString() {
        return " backimg:" + backimg + " content:" + content + " title=" + title + " id:" + id  + "url:"
                + url ;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj instanceof NoticeItem){
            return  ((NoticeItem)obj).id.equals(id);
        }

        return  false;
    }
}