package com.hochan.sqlite.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hochan.sqlite.BroadcastReceiver.NetworkChangeReceiver;
import com.hochan.sqlite.MainActivity;
import com.hochan.sqlite.UI.FileUI;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.data.ThreadInfo;
import com.hochan.sqlite.db.ThreadDAOImpl;
import com.hochan.sqlite.tools.SQLHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DownloadTask {
    private Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAOImpl mThreadDAO = null;
    private long mFinished = 0;
    private int mThreadCount = DownloadService2.runThreadCount;
    public boolean isPause = false;
    //线程池
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    private List<DownloadThread> mThreadList = null;

    public DownloadTask(Context mContext, FileInfo mFileInfo, int mThreadCount) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadDAO = new ThreadDAOImpl(mContext);
        this.mThreadCount = mThreadCount;
    }

    public void download(){
        //读取数据库中线程信息
        List<ThreadInfo> threadInfos = mThreadDAO.getThread(mFileInfo.getFileName(), 0);
        Log.d("mFileInfo name", mFileInfo.getFileName());
        Log.d("mFileInfo size", String.valueOf(mFileInfo.getFinish()));
        Log.d("threadsize=", threadInfos.size() + "");

        if (threadInfos.size() == 0) {
            // 获取每个线程下载的长度
            long length = mFileInfo.getLength() / mThreadCount;
            for(int i=0; i < mThreadCount; i++) {
                ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getFileName(), length * i, (i + 1) * length - 1, 0, 0);
                if (i + 1 == mThreadCount) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }

                //添加线程信息到集合
                threadInfos.add(threadInfo);

                //向数据库中插入线程信息
                mThreadDAO.insertThread(threadInfo);
            }
        } else {
           // Log.d("exist ",)
        }
        mThreadList = new ArrayList<>();
        //启动多个线程下载
        for (ThreadInfo threadInfo : threadInfos) {
            DownloadThread downloadThread = new DownloadThread(threadInfo);
            DownloadTask.executorService.execute(downloadThread);
            Log.d("threadInfo finish", String.valueOf(threadInfo.getFinish()));
            //添加线程到集合
            mThreadList.add(downloadThread);
        }
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread{
        private ThreadInfo threadInfo;
        public boolean isFinished = false;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

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

                SharedPreferences sharedPreferences= mContext.getSharedPreferences(mFileInfo.getFileName(),
                        mContext.MODE_PRIVATE);
                long start = sharedPreferences.getLong("finish",0);
                //设置下载位置
                //long start = threadInfo.getStart() + threadInfo.getFinish();
                Log.d("my start", String.valueOf(start));
                connection.setRequestProperty("Range","bytes=" + start + "-" + threadInfo.getEnd());

                //传入JSONObject
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                JSONObject jsonObject = new JSONObject();
                //jsonObject.put("userId", MainActivity.localWorker.getmID());
                //debug
                jsonObject.put("userId", "123456");
                jsonObject.put("path", mFileInfo.getPath());
                jsonObject.put("fileName", threadInfo.getFileName());
                jsonObject.put("size",mFileInfo.getLength());
                OutputStream wr = connection.getOutputStream();
                wr.write(jsonObject.toString().getBytes("UTF-8"));
                wr.close();

                //设置文件写入位置
                File file = new File(DownloadService2.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                Intent intent = new Intent(DownloadService2.ACTION_UPDATE);
                mFinished += start;
                Log.d("threadInfo.getFinish=",threadInfo.getFinish()+" ");
                Log.d("response code",String.valueOf(connection.getResponseCode()));
                if (connection.getResponseCode() == 200) {
                    Log.e("getContentLength = ", connection.getContentLength() + "");
                    Log.d("size",String.valueOf(mFileInfo.getLength()));
                    is = connection.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    //byte[] bigBuffer = new byte[Integer.parseInt(String.valueOf(mFileInfo.getLength()))];
                   /* byte[] bigBuffer = new byte[Integer.parseInt(String.valueOf(connection.getContentLength()))];*/
                    Log.d("connection content  ",String.valueOf(connection.getContentLength()));
                    int offset = 0;
                    int len = -1;
                    long time = System.currentTimeMillis();
                    String fileGet ="";

                    while ((len = is.read(buffer)) != -1) {

                        if (isPause || !NetworkChangeReceiver.NetworkConnected) {
                            Log.d("mfinished==pause==", mFinished + "");
                            //暂停下载时，保存进度到数据库
                            SharedPreferences sharedPreferences1 = mContext.getSharedPreferences(mFileInfo.getFileName(),mContext.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                            editor.putLong("finish", threadInfo.getFinish());
                            editor.commit();
                            /*mThreadDAO.updateThread(mFileInfo.getFileName(), mFileInfo.getId(), threadInfo.getFinish(), 0);
                            Log.d("get finish",String.valueOf(threadInfo.getFinish()));
                            ThreadInfo tmp = mThreadDAO.getThread(mFileInfo.getFileName(), 0).get(0);
                            Log.d("get 2finish",String.valueOf(tmp.getFinish()));*/
                            return;
                        }

                        //写入文件
                       /* System.arraycopy(buffer, 0, bigBuffer, offset, len);
                        offset += len;
                        Log.d("llal",mFileInfo.getLength()+" "+offset +" "+len);*/

                        raf.write(buffer, 0, len);

                        //累加整个文件的下载进度
                        mFinished += len;
                        FileUI.totalDown += len;
                        //累加每个线程完成的进度
                        //threadInfo.setFinish(threadInfo.getFinish() + len);
                        threadInfo.setFinish(mFinished);

                        //每隔1秒刷新一次UI
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            //把下载进度通过广播发送给ativity
                            intent.putExtra("id", mFileInfo.getId());
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            Log.d(" mFinished==update==", mFinished * 100 / mFileInfo.getLength() +"");
                        }
                    }
                   /* String content = new String(bigBuffer);
                    byte[] data = new byte[content.length()];
                    for (int i = 0; i<content.length(); i++) {
                        data[i] = (byte) content.charAt(i);
                    }
                    Log.d("content length", String.valueOf(content.length()));
                    //把String写入文件中
                    raf.write(data, 0, content.length());*/
                    //表示该线程执行完毕
                    isFinished = true;
                    //检查下载任务是否完成
                    checkAllThreadFinish();

                    is.close();
                }
                raf.close();
                connection.disconnect();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查所有线程是否执行完毕
     */
    private synchronized  void checkAllThreadFinish(){
        boolean allFinished = true;
        //遍历线程集合，判断是否执行完毕
        for (DownloadThread thread : mThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }

        if (allFinished) {
            //删除线程信息
            mThreadDAO.deleteThread(mFileInfo.getFileName(), 0);
            //发送广播给Activity下载结束
            Intent intent = new Intent(DownloadService2.ACTION_FINISHED);
            intent.putExtra("fileinfo", mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }
}
