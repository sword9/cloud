package com.hochan.sqlite.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.hochan.sqlite.BroadcastReceiver.NetworkChangeReceiver;
import com.hochan.sqlite.UI.FileUI;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.service.DownloadService2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DownloadTool  {
    FileInfo fileInfo = null;
    String path = null;
    Context mContext;

    public DownloadTool(FileInfo fileInfo, String path, Context context) {
        this.fileInfo = fileInfo;
        this.path = path;
        this.mContext = context;
    }

    public void beginDownload(){

        java.io.File dir = new File(path);
        Log.d("path", path);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return;
            }
        }
        Log.d("not", "return");
        //在本地创建文件
        File file = new File(dir, fileInfo.getFileName());
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.setLength(fileInfo.getLength());
            //设置本地文件长度
            Log.d("tFileInfo.getLength=",fileInfo.getLength() + "");

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                RandomAccessFile raf;
                InputStream is;

                try {
                    //下载文件
                    URL url = new URL(SQLHttpClient.BASE_URL+"downloadFile");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept-Encoding", "identity");

                    long start = 0;
                    //设置下载位置
                    //long start = threadInfo.getStart() + threadInfo.getFinish();
                    Log.d("my start", String.valueOf(start));
                    connection.setRequestProperty("Range","bytes=" + start + "-" + fileInfo.getLength());

                    //传入JSONObject
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    JSONObject jsonObject = new JSONObject();
                    //jsonObject.put("userId", MainActivity.localWorker.getmID());
                    //debug
                    jsonObject.put("userId", "123456");
                    jsonObject.put("path", fileInfo.getPath());
                    jsonObject.put("fileName", fileInfo.getFileName());
                    jsonObject.put("size",fileInfo.getLength());
                    OutputStream wr = connection.getOutputStream();
                    wr.write(jsonObject.toString().getBytes("UTF-8"));
                    wr.close();

                    //设置文件写入位置
                    File file = new File(path, fileInfo.getFileName());
                    raf = new RandomAccessFile(file, "rwd");
                    raf.seek(start);

                    Log.d("response code",String.valueOf(connection.getResponseCode()));
                    if (connection.getResponseCode() == 200) {
                        Log.e("getContentLength = ", connection.getContentLength() + "");
                        Log.d("size",String.valueOf(fileInfo.getLength()));
                        is = connection.getInputStream();
                        byte[] buffer = new byte[1024 * 4];
                        Log.d("connection content  ",String.valueOf(connection.getContentLength()));
                        int offset = 0;
                        int len = -1;
                        long time = System.currentTimeMillis();
                        String fileGet ="";

                        int count = 0;
                        while ((len = is.read(buffer)) != -1) {
                            raf.write(buffer, 0, len);
                            Log.d("count",""+(count++));
                        }
                        is.close();
                    }
                    raf.close();
                    connection.disconnect();
                    Log.d("下载","完成");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mthread.start();
    }

}
