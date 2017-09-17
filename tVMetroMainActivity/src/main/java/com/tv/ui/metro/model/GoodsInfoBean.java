package com.tv.ui.metro.model;

import com.tv.ui.metro.utils.GsonUtils;

import java.io.Serializable;

/**
 * Created by Lenovo on 2017/5/11.
 */
public class GoodsInfoBean  {

    private String  commodityName;
    private String  sellingPrice;
    private String  qrCode;
    private String  previewImg;
    private String  commodityImgs;
    private String shopName;
    private String commodityDesc;
    private String commodityId;

    public GoodsInfoBean(String commodityName, String sellingPrice, String qrCode, String previewImg, String commodityImgs, String shopName, String commodityDesc, String commodityId) {
        this.commodityName = commodityName;
        this.sellingPrice = sellingPrice;
        this.qrCode = qrCode;
        this.previewImg = previewImg;
        this.commodityImgs = commodityImgs;
        this.shopName = shopName;
        this.commodityDesc = commodityDesc;
        this.commodityId = commodityId;
    }

    @Override
    public String toString() {
        return "GoodsInfoBean{" +
                "commodityName='" + commodityName + '\'' +
                ", sellingPrice='" + sellingPrice + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", previewImg='" + previewImg + '\'' +
                ", commodityImgs='" + commodityImgs + '\'' +
                ", shopName='" + shopName + '\'' +
                ", commodityDesc='" + commodityDesc + '\'' +
                ", commodityId='" + commodityId + '\'' +
                '}';
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPreviewImg() {
        return previewImg;
    }

    public void setPreviewImg(String previewImg) {
        this.previewImg = previewImg;
    }

    public String getCommodityImgs() {
        return commodityImgs;
    }

    public void setCommodityImgs(String commodityImgs) {
        this.commodityImgs = commodityImgs;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

//    public GoodsInfoBean clone() {
//        GoodsInfoBean item = new GoodsInfoBean();
//        item.commodityName = this.commodityName;
//        item.sellingPrice = this.sellingPrice;
//        item.qrCode = this.qrCode;
//        item.previewImg = this.previewImg;
//        item.commodityImgs=this.commodityImgs;
//        item.shopName=this.shopName;
//        item.commodityDesc=this.commodityDesc;
//        item.commodityId=this.commodityId;
//        return item;
//    }
//
//    public String toString() {
//        return " commodityName:" + commodityName + " sellingPrice:" + sellingPrice + " qrCode=" + qrCode + " previewImg:" + previewImg  + "commodityImgs:"
//                + commodityImgs +"shopName:"+shopName+"commodityDesc:"+commodityDesc+"commodityId:"+commodityId;
//    }

    public static GoodsInfoBean parseData(String json) {
        return  GsonUtils.toBean(json, GoodsInfoBean.class);

    }
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == null)
//            return false;
//
//
//        return  false;
//    }
}

