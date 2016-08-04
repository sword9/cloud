package com.hochan.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.File;

import com.hochan.sqlite.data.FileInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/7/18.
 */
public class DownloadService2 extends Service {

    public static final int runThreadCount = 1;
    private static final String TAG = "DownloadService2";

    //初始化
    public static final int MSG_INIT = 0x2;
    //开始下载
    public static final String ACTION_START = "ACTION_START";
    //暂停下载
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    //结束下载
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    //更新UI
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    //下载路径
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads";

    //下载任务集合
    private Map<Integer,DownloadTask> tasks = new LinkedHashMap<>();

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
            Log.d("TAG", "onStartCommand:ACTION_START- " + fileInfo.toString());
            new InitThread(fileInfo).start();

        } else if (ACTION_PAUSE.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            Log.d(TAG, "onStartCommand:ACTION_PAUSE" + fileInfo.toString());
            //从集合中取出任务
            DownloadTask task = tasks.get(fileInfo.getId());
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
                    DownloadTask downloadTask = new DownloadTask(DownloadService2.this, fileInfo,runThreadCount);
                    downloadTask.download();
                    //将下载任务添加到集合中
                    tasks.put(fileInfo.getId(), downloadTask);
                    break;

            }
        }
    };

    /**
     * 初始化 子线程
     */
    class InitThread extends Thread{
        private FileInfo tFileInfo;

        public InitThread(FileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        @Override
        public void run() {
            Log.d("begin","thread");
            RandomAccessFile raf;
            java.io.File dir = new File(DOWNLOAD_PATH);
            Log.d("path", DOWNLOAD_PATH);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    return;
                }
            }
            Log.d("not", "return");
            //在本地创建文件
            File file = new File(dir, tFileInfo.getFileName());
            try {
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(tFileInfo.getLength());
                //设置本地文件长度
                Log.d("tFileInfo.getLength=",tFileInfo.getLength() + "");
                mHandler.obtainMessage(MSG_INIT, tFileInfo).sendToTarget();

                raf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
