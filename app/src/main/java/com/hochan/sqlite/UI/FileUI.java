package com.hochan.sqlite.UI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hochan.sqlite.Filechose.FileExplorerTabActivity;
import com.hochan.sqlite.MainActivity;
import com.hochan.sqlite.R;
import com.hochan.sqlite.adapter.FileListAdapter;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.service.DownloadService;
import com.hochan.sqlite.service.DownloadService2;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * 逻辑文件存储界面
 * Created by Administrator on 2016/7/19.
 */
public class FileUI extends Activity {

    private static final String TAG = "FileUI";

    static public long downloadSpeed = 0;

    ListView listView;
    Button btn;
    Button downloadSpeedBtn;
    Button createFloder;

    public List<FileInfo> fileList;
    static public String localDir="/";
    private FileListAdapter listAdapter;
    public int fileNum = 0;
    private final static int MSG_UPDATEUI=0x12;

    private long time = 0;
    public static long totalDown = 0;
    public long tmpTotal = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileui);

        createFloder = (Button) findViewById(R.id.createFolder);
        downloadSpeedBtn = (Button) findViewById(R.id.downloadSpeed);
        listView = (ListView) findViewById(R.id.filelist);
        btn = (Button) findViewById(R.id.fileBroswer);   //浏览本地文件进行上传
        downloadSpeedBtn.setText(String.valueOf(downloadSpeed )+"  b/s");
        initData();
        initSetup();
        initRegister();

        //启动文件选择器
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileUI.this, FileExplorerTabActivity.class);
                startActivity(intent);
            }
        });
        //发送“创建文件请求” 到服务器
        createFloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.createfloder,
                        (ViewGroup) findViewById(R.id.dialog));

                AlertDialog.Builder builder = new AlertDialog.Builder(FileUI.this);
                builder.setTitle("输入文件名").setView(layout).setNegativeButton("取消", null);
                /*new AlertDialog.Builder(getApplicationContext()).setTitle("输入文件名").setView(layout)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null).show();*/
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText tvName = (EditText) layout.findViewById(R.id.etname);
                        final String filename = tvName.getText().toString();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("userId","123456");
                            jsonObject.put("path",FileUI.localDir+filename);
                            StringEntity stringEntity = new StringEntity(jsonObject.toString());
                            SQLHttpClient.post(FileUI.this, SQLHttpClient.BASE_URL+"addDirectory",stringEntity, RequestParams.APPLICATION_JSON,
                                    new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            Toast.makeText(FileUI.this,"创建成功",Toast.LENGTH_SHORT).show();
                                            fileList.add(new FileInfo(localDir, filename, 0, fileNum++,true, 0));
                                            //listAdapter.notifyDataSetChanged();
                                            mHandler.obtainMessage(MSG_UPDATEUI, fileList).sendToTarget();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);

                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
            }
        });

        //点击item，如果item为文件夹则访问服务器，获取新的文件，更新fileList
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo fileInfo = fileList.get(position);
                if(fileInfo.isDir())
                {
                    Log.d("点击",fileInfo.toString());
                    if(fileInfo.getFileName()==""){
                        localDir ="/";
                    } else {
                        localDir = fileInfo.getPath()+fileInfo.getFileName()+"/";
                    }

                    try {
                         getFileList(localDir); //向服务器端请求云端文件列表
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initRegister(){
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService2.ACTION_UPDATE);
        filter.addAction(DownloadService2.ACTION_FINISHED);
        registerReceiver(mReceiver, filter);
    }


    private void initData(){
        fileList = new ArrayList<>();
        FileInfo fileInfo = new FileInfo(localDir, "", 0, fileNum, true, 0);
        fileList.add(fileInfo);
    }

    private void initSetup(){
        //创建适配器
        listAdapter = new FileListAdapter(this, fileList);
        //给listView设置适配器
        listView.setAdapter(listAdapter);

        time = System.currentTimeMillis();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATEUI:
                    fileList = (List<FileInfo>) msg.obj;
                    Log.d("file",fileList.size()+"");
                    listAdapter = new FileListAdapter(FileUI.this, fileList);
                    //给listView设置适配器
                    listView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    //打印获取到的文件列表
                    for(int i=0; i<fileList.size(); i++) {
                        FileInfo fileInfo = fileList.get(i);
                        Log.d("FileUI", fileInfo.toString());
                    }


            }
        }
    };



    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService2.ACTION_UPDATE.equals((intent.getAction()))) {
                long finished = intent.getLongExtra("finished", 0);
                int id = intent.getIntExtra("id", 0);
                Log.d(TAG, "finished==" + finished);
                Log.d(TAG, "id==" + id);
                listAdapter.updateProgress(id, finished);
                //更新下载速度
                long tmpTime = System.currentTimeMillis();
                downloadSpeedBtn.setText(String.valueOf((totalDown - tmpTotal)/((tmpTime - time)))+"  kb/s");
                tmpTotal = totalDown;
                time = tmpTime;

            } else if (DownloadService2.ACTION_FINISHED.equals(intent.getAction())) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
                //更新进度条为100
                listAdapter.updateProgress(fileInfo.getId(), 100);
                Toast.makeText(FileUI.this, "下载完成", Toast.LENGTH_SHORT).show();
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

    private List<FileInfo> getFileList(String path) throws JSONException, UnsupportedEncodingException {
        final List<FileInfo> fileInfos = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        /*jsonObject.put("userId", MainActivity.localWorker.getmID());*/
        //调试专用
        jsonObject.put("userId", "123456");
        jsonObject.put("path", path);
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        SQLHttpClient.post(getApplicationContext(), SQLHttpClient.BASE_URL + "listAll",stringEntity, RequestParams.APPLICATION_JSON,
                new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            //debug
                            Log.d("response", response.toString());

                            fileNum = 0;
                            JSONArray jsonArray =  response.getJSONArray("directory");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                FileInfo fileInfo = new FileInfo(localDir, (String) jsonArray.get(i), 0, fileNum, true, 0);
                                fileNum++;
                                fileInfos.add(fileInfo);
                                Log.d("fileInfo dir",fileInfo.toString());
                            }
                            jsonArray = response.getJSONArray("file");
                            JSONArray sizeArray = response.getJSONArray("size");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //文件大小
                                //String fileSize = (String) sizeArray.get(i);
                                FileInfo fileInfo = new FileInfo(localDir, (String) jsonArray.get(i), 0, fileNum, false, Long.parseLong(String.valueOf(sizeArray.get(i))));
                                fileNum++;
                                fileInfos.add(fileInfo);
                                Log.d("fileInfo file",fileInfo.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mHandler.obtainMessage(MSG_UPDATEUI, fileInfos).sendToTarget();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("get file error","failed");
                    }
                });

        return fileInfos;
    }


}
