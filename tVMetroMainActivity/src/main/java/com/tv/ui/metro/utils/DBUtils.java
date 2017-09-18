package com.tv.ui.metro.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericAlbum;
import com.tv.ui.metro.model.GenericSubjectItem;
import com.tv.ui.metro.model.Image;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.model.PDSBean;
import com.tv.ui.metro.model.TopBar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lml on 17/9/15.
 */

public class DBUtils {



    public static final String SQLITEDBPATH = "/sdcard/zshy_new.sqlite";

    public static final String[] imgs = new String[]{
            "http://sinacloud.net/diancai/1.png",
            "http://sinacloud.net/diancai/2.png",
            "http://sinacloud.net/diancai/3.png",
            "http://sinacloud.net/diancai/4.png",
            "http://sinacloud.net/diancai/5.png",
            "http://sinacloud.net/diancai/6.png",
            "http://sinacloud.net/diancai/7.png",
            "http://sinacloud.net/diancai/8.png",
            "http://sinacloud.net/diancai/9.png",
            "http://sinacloud.net/diancai/10.png",
            "http://sinacloud.net/diancai/11.png",
            "http://sinacloud.net/diancai/12.png",
            "http://sinacloud.net/diancai/13.png",
            "http://sinacloud.net/diancai/14.png"

    };

    public static DisplayItem.UI[] uis = new DisplayItem.UI[15];

    static {
        uis[0] = new DisplayItem.UI();
        uis[0].layout = new DisplayItem.UI.Layout(1,1,1,1);
        uis[0].type = "metro_cell_banner";

        uis[1] = new DisplayItem.UI();
        uis[1].layout = new DisplayItem.UI.Layout(2,1,1,1);
        uis[1].type = "metro_cell_banner";

        uis[2] = new DisplayItem.UI();
        uis[2].layout = new DisplayItem.UI.Layout(5,1,1,1);
        uis[2].type = "metro_cell_banner";

        uis[3] = new DisplayItem.UI();
        uis[3].layout = new DisplayItem.UI.Layout(6,1,1,1);
        uis[3].type = "metro_cell_banner";

        uis[4] = new DisplayItem.UI();
        uis[4].layout = new DisplayItem.UI.Layout(1,2,1,1);
        uis[4].type = "metro_cell_banner";

        uis[5] = new DisplayItem.UI();
        uis[5].layout = new DisplayItem.UI.Layout(2,2,1,1);
        uis[5].type = "metro_cell_banner";

        uis[6] = new DisplayItem.UI();
        uis[6].layout = new DisplayItem.UI.Layout(5,2,1,1);
        uis[6].type = "metro_cell_banner";

        uis[7] = new DisplayItem.UI();
        uis[7].layout = new DisplayItem.UI.Layout(6,2,1,1);
        uis[7].type = "metro_cell_banner";


        uis[8] = new DisplayItem.UI();
        uis[8].layout = new DisplayItem.UI.Layout(1,3,1,1);
        uis[8].type = "metro_cell_banner";

        uis[9] = new DisplayItem.UI();
        uis[9].layout = new DisplayItem.UI.Layout(2,3,1,1);
        uis[9].type = "metro_cell_banner";

        uis[10] = new DisplayItem.UI();
        uis[10].layout = new DisplayItem.UI.Layout(3,3,1,1);
        uis[10].type = "metro_cell_banner";

        uis[11] = new DisplayItem.UI();
        uis[11].layout = new DisplayItem.UI.Layout(4,3,1,1);
        uis[11].type = "metro_cell_banner";

        uis[12] = new DisplayItem.UI();
        uis[12].layout = new DisplayItem.UI.Layout(5,3,1,1);
        uis[12].type = "metro_cell_banner";

        uis[13] = new DisplayItem.UI();
        uis[13].layout = new DisplayItem.UI.Layout(6,3,1,1);
        uis[13].type = "metro_cell_banner";



    }


    public static GenericSubjectItem<DisplayItem> getDisplayItemsByGroupId(String groupid, String Category){
        GenericSubjectItem<DisplayItem> data = new GenericSubjectItem
                <DisplayItem>() ;

        data.topbar = new TopBar();
        data.topbar.logo = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2961051620,905474745&fm=27&gp=0.jpg";
        data.topbar.title = "江西赣州";
        data.topbar.bg = "";
        data.topbar.welcomebg = "";

        data.data = new ArrayList<GenericAlbum<DisplayItem>>();
        ArrayList<PDSBean> beans = new ArrayList<PDSBean>();

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(SQLITEDBPATH, null);
        Cursor cursor = null;
        if( groupid == null || TextUtils.isEmpty(groupid)){
            cursor = db.rawQuery("select  code,Category, Company,CalledNum, CalledPassword, CallingNum, CallingPassword,HasChildren from t_Directory where Category=?", new String[]{Category});
        }else{
            cursor = db.rawQuery("select code,Category, Company,CalledNum, CalledPassword, CallingNum, CallingPassword,HasChildren from t_Directory where ParentCode=?", new String[]{groupid});
        }
        while (cursor.moveToNext()) {
            PDSBean temp = new PDSBean();
            temp.setCode(cursor.getString(0));
            temp.setCategory(cursor.getString(1));
            temp.setCompany(cursor.getString(2));
            temp.setCalledNum(cursor.getString(3));
            temp.setCalledPassword(cursor.getString(4));
            temp.setCallingNum(cursor.getString(5));
            temp.setCallingPassword(cursor.getString(6));
            temp.setHaschild(cursor.getInt(7) == 1);

            beans.add(temp);
        }
        for (int i= 0 ;i<= beans.size()/imgs.length;i++){
            GenericAlbum<DisplayItem> temp = new GenericAlbum<DisplayItem>();
            temp.items = new ArrayList<DisplayItem>();
            temp.type = "board";
            temp.wc = 6;
            temp.hc = 3 ;
            temp.name = "第"+(i+1)+"页";
            temp.ns = "app";
            temp.id = ""+i;
//            "_ui": {
//                "type": "metro"
//            }
            data.data.add(temp);
        }
        for (int i= 0 ;i< beans.size(); i++){
            DisplayItem temp = new DisplayItem();
//            temp.type = beans.get(i).getCategory();
            if(beans.get(i).isHaschild()){
                temp.type = "open_group";
                temp.target = new DisplayItem.Target();
                temp.target.jump_uri = beans.get(i).getCode();
            }else{
                temp.type = "open_zhibo";
            }
            temp.name = beans.get(i).getCompany();
            temp.id = beans.get(i).getCode();
            temp.ns = "app";
            temp.focusable = "1";

            temp._ui = uis[i%imgs.length];
            ImageGroup imggroup = new ImageGroup();
            imggroup.put("text", new Image(beans.get(i).getCompany()));
            imggroup.put("back", new Image(imgs[i%imgs.length]));
            temp.images = imggroup ;





//            "target": {
//                "jump_uri": "http://10.254.203.196:8081/panda/cms/noticeListByMac",
//                        "param": "",
//                        "subType": "single",
//                        "type": "open_url"
//            },
//            "images": {
//                "text": {
//                    "ani": {},
//                    "url": "市级面对面",
//                    "pos": {}
//                },
//                "back": {
//                    "ani": {},
//                    "url": "http://10.254.203.196:8081/panda/cms/images/2017/09/12/150518111936372e84d72-b313-4cea-8538-a52a1f074e21.png",
//                            "pos": {}
//                },


            data.data.get(i/imgs.length).items.add(temp);
        }





        return data ;
    }


    public static boolean hasChild(PDSBean bean){
        boolean result = true ;



        return result ;
    }


}
