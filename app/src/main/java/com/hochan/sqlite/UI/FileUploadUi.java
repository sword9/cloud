package com.hochan.sqlite.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hochan.sqlite.R;
import com.hochan.sqlite.adapter.FileListAdapter;
import com.hochan.sqlite.adapter.UploadFileAdapter;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.service.UploadService2;
import com.hochan.sqlite.Filechose.myFileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/23.
 */
public class FileUploadUi extends AppCompatActivity {
    private static final String TAG = "FileUI";

    ListView listView;

    public List<FileInfo> fileList;
    public String localDir="/";
    private UploadFileAdapter listAdapter;
    private final static int MSG_UPDATEUI=0x2;

    private TextView tv_uploadSpeed ;

    private long time = 0;
    public static long totalUpload = 0;
    private long tmpDown = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadui);
        //用于显示当前上传速度
        tv_uploadSpeed = (TextView) findViewById(R.id.uploadSpeed);
        time = System.currentTimeMillis();
        tv_uploadSpeed.setText("0  b/s");

        fileList = new ArrayList<>();
        myFileInfo fileInfo = (myFileInfo) getIntent().getSerializableExtra("fileinfo");
        FileInfo fi = new FileInfo(fileInfo.getPath(), fileInfo.getFileName(), fileInfo.getFinish(), 0, false, fileInfo.getLength());
        fileList.add(fi);
        Log.d("fileinfo",fileInfo.toString());
        listView = (ListView) findViewById(R.id.upload_filelist);

        initData();
        initSetup();
        initRegister();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATEUI:
                    fileList = (List<FileInfo>) msg.obj;
                    Log.d("file",fileList.size()+"");
                    listAdapter = new UploadFileAdapter(FileUploadUi.this, fileList);
                    //给listView设置适配器
                    listView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
            }
        }
    };

    private void initRegister(){
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(UploadService2.ACTION_START);
        filter.addAction(UploadService2.ACTION_UPDATE);
        filter.addAction(UploadService2.ACTION_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    private void initData(){

    }

    private void initSetup(){
        //创建适配器
        listAdapter = new UploadFileAdapter(this, fileList);
        //给listView设置适配器
        listView.setAdapter(listAdapter);
    }

    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UploadService2.ACTION_UPDATE.equals((intent.getAction()))) {
                long finished = intent.getLongExtra("finished", 0);
                int id = intent.getIntExtra("id", 0);
                Log.d(TAG, "finished==" + finished);
                Log.d(TAG, "id==" + id);
                listAdapter.updateProgress(id, finished);
                //更新显示的上传速度
                long tmptime = System.currentTimeMillis();
                tv_uploadSpeed.setText(String.valueOf((totalUpload - tmpDown)/(tmptime - time)) + "  kb/s");
                time = tmptime;
                tmpDown = totalUpload;

            } else if (UploadService2.ACTION_FINISHED.equals(intent.getAction())) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
                //更新进度条为100
                listAdapter.updateProgress(fileInfo.getId(), 100);
                Toast.makeText(FileUploadUi.this, "上传完成", Toast.LENGTH_SHORT).show();
            } else if(intent.getAction() == "UPLOAD_ACTION_START"){
                Log.d("madan", "get broadcast");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

}
