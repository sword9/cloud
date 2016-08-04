package com.hochan.sqlite.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hochan.sqlite.BroadcastReceiver.NetworkChangeReceiver;
import com.hochan.sqlite.UI.FileUI;
import com.hochan.sqlite.UI.FileUploadUi;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.data.ThreadInfo;
import com.hochan.sqlite.db.ThreadDAOImpl;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Administrator on 2016/7/23.
 */
public class UploadTask {
    private Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAOImpl mThreadDAO = null;
    private long mFinished = 0;
    public boolean isPause = false;
    private UploadThread mThread = null;
    public int upload_begin = 0;

    public int buffer_size = 1024 * 4;
    //线程池

    public UploadTask(Context mContext, FileInfo mFileInfo, int upload_begin) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadDAO = new ThreadDAOImpl(mContext);
        this.upload_begin = upload_begin;
    }

    public void upload(){
        List<ThreadInfo> threadInfoList = mThreadDAO.getThread(mFileInfo.getFileName(),1);

        if (threadInfoList.size() == 0) {
            ThreadInfo threadInfo = new ThreadInfo(1, mFileInfo.getFileName(), 0, 0, 1, 0);
            mThreadDAO.insertThread((threadInfo));
            threadInfoList.add(threadInfo);
        }


        for (ThreadInfo threadInfo : threadInfoList) {
            new UploadThread(threadInfo, mContext).start();
        }
    }


    class UploadThread extends Thread{
        private ThreadInfo threadInfo;
        public boolean isFinished = false;
        public Context mContext;

        public UploadThread(ThreadInfo threadInfo,Context context) {
            this.threadInfo = threadInfo;
            this.mContext = context;
        }

        @Override
        public void run() {
            RandomAccessFile raf;

            try {

                java.io.File file =new File(mFileInfo.getPath());
                raf = new RandomAccessFile(file,"rwd");
                //raf.seek(upload_begin);
                Log.d("file size", raf.length() + "");
                Log.d("begin","thread");
                long time = System.currentTimeMillis();
                while (!isFinished) {
                    if(raf.length() - upload_begin < buffer_size){
                        isFinished = true;
                        buffer_size = Integer.parseInt(String.valueOf(raf.length())) - upload_begin;
                        Log.d("buffer size", buffer_size+"");
                    }
                    byte[] buffer = new byte[buffer_size+100];
                    raf.seek(upload_begin);
                    raf.readFully(buffer,0, buffer_size);
                    //更新总上传速度
                    FileUploadUi.totalUpload += buffer_size;
                    JSONObject jsonObject1 = new JSONObject();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < buffer_size; ++i)
                        builder.append((char)(buffer[i]));
                    String result = builder.toString();
                    //用于debug
                    //用于传输的JSONObject
                    jsonObject1.put("userId","123456");
                    jsonObject1.put("path",FileUI.localDir);
                    jsonObject1.put("fileName",mFileInfo.getFileName());
                    jsonObject1.put("size", raf.length());
                    jsonObject1.put("content", result);


                    Log.d("content大小", result.length() + "");

                    //上传
                    URL url = new URL(SQLHttpClient.BASE_URL+"uploadFile");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    OutputStream wr = connection.getOutputStream();
                    wr.write(jsonObject1.toString().getBytes());
                    wr.close();
                    Log.d("upload response code", String.valueOf(connection.getResponseCode()));
                    connection.disconnect();
                    upload_begin += buffer_size;
                    //传输完成，更新UI更新数据库
                    if (isPause && NetworkChangeReceiver.NetworkConnected) {
                        Log.d("mfinished==pause==", mFinished + "");
                        //暂停下载时，保存进度到数据库
                        mThreadDAO.updateThread(mFileInfo.getFileName(), mFileInfo.getId(), threadInfo.getFinish(),1);
                        return;
                    }
                    Intent intent = new Intent(UploadService2.ACTION_UPDATE);
                    //每隔1秒刷新一次UI
                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        //把下载进度通过广播发送给ativity
                        intent.putExtra("id", mFileInfo.getId());
                        intent.putExtra("finished", upload_begin * 100 / mFileInfo.getLength());
                        mContext.sendBroadcast(intent);
                        Log.d(" mFinished==update==", upload_begin * 100 / mFileInfo.getLength() +"");
                    }
                }
                raf.close();
                mThreadDAO.deleteThread(mFileInfo.getFileName(), 1);
                Intent intent = new Intent(UploadService2.ACTION_FINISHED);
                intent.putExtra("fileinfo", mFileInfo);
                mContext.sendBroadcast(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
