package com.hochan.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hochan.multi_file_selector.MultiFileSelectorFragment;
import com.hochan.sqlite.MainActivity;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.hochan.sqlite.tools.Tool;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Administrator on 2016/7/7.
 */
public class UpLoadService extends Service{

    static final int smallSize = 100 * 1024;

    ArrayList<String> filePaths;
    Binder mBinder=new UpLoadBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        filePaths = new ArrayList<String>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        filePaths = intent.getStringArrayListExtra(MultiFileSelectorFragment.EXTRA_RESULT);
        for(String path : filePaths)
        {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", MainActivity.localWorker.getmID());
                jsonObject.put("fileName", Tool.getFileName(path));
               // Log.d("path",path);
                java.io.File file = new java.io.File(path);
                jsonObject.put("content",Tool.getStringFromFile(path));
               // Log.d("contentOfImage",Tool.getStringFromFile(path));
                jsonObject.put("path",path);
                jsonObject.put("size",file.getTotalSpace());
                //小文件传输 size <= 100KB
                if(file.getTotalSpace() <= smallSize){
                    StringEntity stringEntity = new StringEntity(jsonObject.toString());
                    SQLHttpClient.post(getApplicationContext(),SQLHttpClient.BASE_URL+"/uploadSmallFile", stringEntity, RequestParams.APPLICATION_JSON,
                            new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    super.onSuccess(statusCode, headers, response);
                                    Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    String str = SQLHttpClient.handleFailure(statusCode, headers, responseString, throwable);
                                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                                }
                            });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class UpLoadBinder extends Binder{
        public UpLoadService getService(){
            return UpLoadService.this;
        }

    }
}
