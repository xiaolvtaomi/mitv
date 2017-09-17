package com.tv.ui.metro.view;


import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lml on 16/9/7.
 */
public class EncrypePreRequest {
    private static final String token = "f0540c58cd3ca1825cc8e79bbe32952";

    //1，按key排序，2，拼接key－value组成字符串, 4 str 进行MD5加密；5，加密结果转大写；3，加密结果签名 str；6，MD5加密；
    public static String encrypePreQuest(HashMap<String, Object> params){
        Map<String, Object> sortedParams = sortMapByKey(params);
        String temp = mapToString(sortedParams);
        System.out.println(""+temp);
        String temp1 = UpperString(MD5(temp));
        System.out.println(""+temp1);
        String temp2 = UpperString(MD5(temp1+token));
        System.out.println(""+temp2);

        temp = null;
        temp1 = null ;
        return temp2;
    }


    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    public static String mapToString(Map<String, Object> sortedParams){
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = sortedParams.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            sb.append(key);
            sb.append(sortedParams.get(key));
        }
        return sb.toString();
    }

    private static String MD5(String str){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    private static String UpperString(String str){
        return str.toUpperCase();
    }



}
