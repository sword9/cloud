package com.hochan.sqlite.tools;

import android.os.Environment;

import com.hochan.sqlite.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Administrator on 2016/7/14.
 */
public class DownloadHelper extends Thread {
    private String fileName;
    private String path;
    private String AddUrl;
    private long size;
    private long localSize;
    private final long blockSize = 1024 * 1024;


    DownloadHelper(String fileName, String path,String AddUrl,long rangeLow, long rangeHigh) {
        this.fileName = fileName;
        this.path = path;
        this.AddUrl = AddUrl;


    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", MainActivity.localWorker.getmID());
            jsonObject.put("fileName",fileName);
            jsonObject.put("path",path);

            File tmpfile = Environment.getExternalStorageDirectory();
            File myFile = new File(tmpfile.getAbsolutePath()+fileName);
            if( !myFile.exists()){
                try {
                    myFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //获取本地文件大小
            FileInputStream fis = new FileInputStream(myFile);
            localSize = fis.available();
            fis.close();

            long begin = localSize;
            long end;
            while(begin < size ){
                if(size > begin + blockSize){
                    end = begin + blockSize;
                } else {
                    end = size;
                }
                RandomAccessFile randomAccessFile = new RandomAccessFile(myFile,"rw");
                randomAccessFile.seek(begin);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
