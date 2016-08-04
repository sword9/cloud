package com.hochan.sqlite.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hochan.sqlite.data.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ThreadDAOImpl implements ThreadDAO {

    private static final String TAG = "ThreadDAOImpl";

    private DownloadDbHelper downloadDbHelper;

    public ThreadDAOImpl(Context context){
        this.downloadDbHelper = DownloadDbHelper.getInstance(context);
    }

    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        Log.d("insertThread: ","insertThread");
        SQLiteDatabase db = downloadDbHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,filename,start,end,upload,finished) values(?,?,?,?,?,?)",
                new Object[]{threadInfo.getId(),threadInfo.getFileName(),threadInfo.getStart(),threadInfo.getEnd(),threadInfo.getUpload(),threadInfo.getFinish()});
        db.close();
    }

    @Override
    public synchronized void deleteThread(String filename, int upload) {
        Log.d("deleteThread: ","deleteThread");
        SQLiteDatabase db = downloadDbHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where filename = ? and upload= ?",
                new Object[]{filename, upload});
    }

    @Override
    public synchronized void updateThread(String filename, int thread_id, long finished, int upload) {
        Log.d("updateThread: ","updateThread");
        SQLiteDatabase db = downloadDbHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished = ? where filename = ? and thread_id= ? and upload= ?",
                new Object[]{finished, filename, thread_id, upload});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThread(String filename, int upload) {
        Log.d("getThread: ","getThread");
        List<ThreadInfo> list = new ArrayList<>();

        SQLiteDatabase db = downloadDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where filename= ? and upload= ?",
                new String[]{filename, upload+""});
        while (cursor.moveToNext()) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            threadInfo.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
            threadInfo.setUpload(cursor.getInt(cursor.getColumnIndex("upload")));
            threadInfo.setFinish(cursor.getLong(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String filename, int thread_id) {
        SQLiteDatabase db = downloadDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where filename= ? and thread_id=?",
                new String[]{filename,String.valueOf(thread_id)});
        boolean isExist= cursor.moveToNext();
        cursor.close();
        db.close();
        Log.d(TAG,"isExists: "+isExist);
        return isExist;
    }
}
