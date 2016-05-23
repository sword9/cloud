package com.hochan.sqlite.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Tool {

    public static String getDateTime() {
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        String ts=timestamp.toString();
        return ts;
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
    }
}
