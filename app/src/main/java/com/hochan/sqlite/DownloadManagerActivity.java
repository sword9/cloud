package com.hochan.sqlite;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hochan.sqlite.adapter.DownloadTaskAdapter;
import com.hochan.sqlite.data.DownloadInfo;
import com.hochan.sqlite.service.DownloadService;
import com.hochan.sqlite.sql.DownloadTaskDataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadManagerActivity extends AppCompatActivity {

    private RecyclerView recyDownloadTask;
    private DownloadTaskAdapter mAdapter;

    private DownloadService mDownloadService;
    private List<String> mDownloadUrls = new ArrayList<>();
    private HashMap<String, DownloadInfo> mDownloadTaskList = new HashMap<>();

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("已绑定service");
            mDownloadService = ((DownloadService.DownloadBinder) service).getService();
            mDownloadService.setDownloadListener(new DownloadService.DownloadServiceListener() {
                @Override
                public void progress(int index, long downloaded, long total) {
                    //System.out.println("下载管理器:"+index+" "+downloaded+" "+total);
                    if(mAdapter.getItemCount() > index){
                        mAdapter.notifyItemChanged(index);
                    }
                }
            });
            int start = mDownloadUrls.size();
            for(String url : mDownloadService.getDownloadUrls()){
                if(mDownloadUrls.contains(url)){
                    mDownloadTaskList.remove(url);
                    mDownloadTaskList.put(url, mDownloadService.getDownloadList().get(url));
                }
                else {
                    mDownloadUrls.add(url);
                    mDownloadTaskList.put(url, mDownloadService.getDownloadList().get(url));
                }
            }
            //mDownloadUrls = mDownloadService.getDownloadUrls();
            //mDownloadTaskList = mDownloadService.getDownloadList();
            //mAdapter.setDownloadTasks(mDownloadUrls, mDownloadTaskList);
            mAdapter.notifyItemRangeChanged(start, mDownloadUrls.size());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDownloadTaskList = DownloadTaskDataHelper.getDownloadTasks(this, mDownloadUrls);
        System.out.println("从数据库读取的下载任务数目："+mDownloadUrls.size());

        recyDownloadTask = (RecyclerView) findViewById(R.id.recy_download_task);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyDownloadTask.setLayoutManager(linearLayoutManager);
        mAdapter = new DownloadTaskAdapter(this);
        mAdapter.setDownloadTasks(mDownloadUrls, mDownloadTaskList);
        recyDownloadTask.setAdapter(mAdapter);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mServiceConn, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
