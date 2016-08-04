package com.hochan.sqlite.UI;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.service.DownloadTask;
import com.hochan.sqlite.tools.DownloadTool;

public class test extends AppCompatActivity {

    Button btn;
    String path = Environment.getExternalStorageDirectory()+"/ftp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.test_Btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FileInfo{id=4, isDir='false, fileName='cloud_pan.apk', length=3588353, finish=0}
                FileInfo fileInfo = new FileInfo("/","cloud_pan.apk",0,4,false,3588353);
                DownloadTool downloadTool = new DownloadTool(fileInfo, path, getApplicationContext());
                downloadTool.beginDownload();
                Log.d("开始","下载");
            }
        });
    }
}
