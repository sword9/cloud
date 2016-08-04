package com.hochan.sqlite.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    //获取路径中的文件名
    public static String getFileName(String path)
    {
        int start = path.lastIndexOf("/");
        if(start != -1)
            return path.substring(start);
        else
            return null;
    }

    //将文件转为字符串
    public static String convertStreamToString(InputStream is) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null){
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception{
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

}


















