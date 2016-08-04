package com.hochan.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hochan.sqlite.UI.FileUI;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Administrator on 2016/7/23.
 */
public class UploadService2 extends Service {

    private static final String TAG = "UploadService2";

    //初始化
    public static final int MSG_INIT = 0x2;

    public static final int MSG_begin = 0x3;
    //开始下载
    public static final String ACTION_START = "UPLOAD_ACTION_START";
    //暂停下载
    public static final String ACTION_PAUSE = "UPLOAD_ACTION_PAUSE";
    //结束下载
    public static final String ACTION_FINISHED = "UPLOAD_ACTION_FINISHED";
    //更新UI
    public static final String ACTION_UPDATE = "UPLOAD_ACTION_UPDATE";

    //上传任务集合
    private Map<Integer,UploadTask> tasks = new LinkedHashMap<>();

    public int upload_begin = 0;

    public FileInfo pubFileInfo;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null == intent || null == intent.getAction ()) {
            String source = null == intent ? "intent" : "action";
            Log.e (TAG, source + " was null, flags=" + flags + " bits=" + Integer.toBinaryString (flags));
            stopSelf();
            return START_NOT_STICKY;
        }
        //获得Activity传来的参数
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            pubFileInfo = fileInfo;
            //向服务器询问从哪里开始上传
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userId","123456");
                jsonObject.put("path", FileUI.localDir);
                jsonObject.put("size",fileInfo.getLength());
                Log.d("upload file size",String.valueOf(fileInfo.getLength()));
                jsonObject.put("fileName", fileInfo.getFileName());
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                SQLHttpClient.post(getApplicationContext(), SQLHttpClient.BASE_URL+"checkFileStatus",stringEntity, RequestParams.APPLICATION_JSON,
                        new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    upload_begin = response.getInt("position");
                                    Log.d("UploadTask", "获取上传位置"+upload_begin);
                                    mHandler.obtainMessage(MSG_begin,upload_begin).sendToTarget();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.d("UploadTask", "获取上传位置失败");
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("TAG", "onStartCommand:ACTION_START- " + fileInfo.toString());
            //new InitThread(fileInfo).start();

        } else if (ACTION_PAUSE.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            Log.d(TAG, "onStartCommand:ACTION_PAUSE" + fileInfo.toString());
            //从集合中取出任务
            UploadTask task = tasks.get(fileInfo.getId());
            if (task != null) {
                task.isPause = true;
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.d("mHandler--fileinfo:", fileInfo.toString());
                    //启动下载任务
                    UploadTask uploadTask = new UploadTask(UploadService2.this, fileInfo, upload_begin);
                    uploadTask.upload();
                    //将下载任务添加到集合中
                    tasks.put(fileInfo.getId(), uploadTask);
                    break;
                case MSG_begin:
                    new InitThread(pubFileInfo).start();
                    break;
            }
        }
    };

    class InitThread extends Thread {
        private FileInfo tFileInfo;

        public InitThread(FileInfo fileInfo) {
            this.tFileInfo = fileInfo;
        }

        @Override
        public void run() {
            Log.d("begin","thread");
            mHandler.obtainMessage(MSG_INIT, tFileInfo).sendToTarget();
        }
    }
}
