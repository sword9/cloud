package com.hochan.sqlite.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Tool {

    private static final long ONE_KB = 1024;
    private static final long ONE_MB = 1024*1024;
    private static final long ONE_GB = 1024*ONE_MB;

    public static String getDateTime() {
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        String ts=timestamp.toString();
        return ts;
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
    }

    public static String getSizeFormat(long size){
        if(size < ONE_KB){
            return String.valueOf(size)+"B";
        }else if(size < ONE_MB){
            return String.valueOf(size/1024)+"KB";
        }else if(size < ONE_GB){
            return String.valueOf(size/ONE_MB)+"MB";
        }else{
            return String.valueOf(size/ONE_GB)+"GB";
        }
    }
}
