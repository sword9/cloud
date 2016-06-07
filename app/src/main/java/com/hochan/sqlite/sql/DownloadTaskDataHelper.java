package com.hochan.sqlite.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hochan.sqlite.data.DownloadInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/7.
 */
public class DownloadTaskDataHelper {

    private SQLiteDatabase mDatabase;
    private static SqliteHelper mHelper;

    public static boolean hasDownloadTask(Context context, DownloadInfo downloadInfo){
        SqliteHelper sqliteHelper = new SqliteHelper(context, DataHelper.DB_NAME, null, SqliteHelper.TB_VERSION);
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.TB_DOWNLOAD_TASK, null, SqliteHelper.URL + "=?",
                new String[]{downloadInfo.getmUrl()}, null, null, null);
        boolean b = cursor.moveToFirst();
        sqLiteDatabase.close();
        sqliteHelper.close();
        return b;
    }

    public static long saveDownloadTask(Context context, DownloadInfo downloadInfo){
        SqliteHelper sqliteHelper = new SqliteHelper(context, DataHelper.DB_NAME, null, SqliteHelper.TB_VERSION);
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.URL, downloadInfo.getmUrl());
        values.put(SqliteHelper.NAME, downloadInfo.getmName());
        values.put(SqliteHelper.PATH, downloadInfo.getmStoragePath());
        values.put(SqliteHelper.STATE, downloadInfo.getmState());
        values.put(SqliteHelper.TOTAL_SIZE, downloadInfo.getmTotalSize());
        long result = 0;
        if(hasDownloadTask(context, downloadInfo)){
            result = sqLiteDatabase.update(SqliteHelper.TB_DOWNLOAD_TASK, values, SqliteHelper.URL + "=?", new String[]{downloadInfo.getmUrl()});
        }else{
            result = sqLiteDatabase.insert(SqliteHelper.TB_DOWNLOAD_TASK, SqliteHelper.URL, values);
        }
        sqLiteDatabase.close();
        sqliteHelper.close();
        return result;
    }

    public static void saveDownloadTaskList(Context context, HashMap<String, DownloadInfo> downloadTaskList, List<String> downloadUrls){
        SqliteHelper sqliteHelper = new SqliteHelper(context, DataHelper.DB_NAME, null, SqliteHelper.TB_VERSION);
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        for(String url : downloadUrls){
            ContentValues values = new ContentValues();
            DownloadInfo downloadInfo = downloadTaskList.get(url);
            values.put(SqliteHelper.URL, downloadInfo.getmUrl());
            values.put(SqliteHelper.NAME, downloadInfo.getmName());
            values.put(SqliteHelper.PATH, downloadInfo.getmStoragePath());
            if(downloadInfo.getmState() == DownloadInfo.STATE_LOADING)
                values.put(SqliteHelper.STATE, DownloadInfo.STATE_UNFINISHED);
            else
                values.put(SqliteHelper.STATE, downloadInfo.getmState());
            values.put(SqliteHelper.TOTAL_SIZE, downloadInfo.getmTotalSize());
            if(hasDownloadTask(context, downloadInfo)){
                sqLiteDatabase.update(SqliteHelper.TB_DOWNLOAD_TASK, values, SqliteHelper.URL + "=?", new String[]{downloadInfo.getmUrl()});
            }else{
                sqLiteDatabase.insert(SqliteHelper.TB_DOWNLOAD_TASK, SqliteHelper.URL, values);
            }
        }
        sqLiteDatabase.close();
        sqliteHelper.close();
    }

    public static HashMap<String, DownloadInfo> getDownloadTasks(Context context, List<String> downloadUrls){
        Map<String, DownloadInfo> downloadTasks = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(context, DataHelper.DB_NAME, null, SqliteHelper.TB_VERSION);
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.TB_DOWNLOAD_TASK, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String url = cursor.getString(cursor.getColumnIndex(SqliteHelper.URL));
            String name = cursor.getString(cursor.getColumnIndex(SqliteHelper.NAME));
            String path = cursor.getString(cursor.getColumnIndex(SqliteHelper.PATH));
            //boolean isFinished = cursor.getInt(cursor.getColumnIndex(SqliteHelper.STATE)) == 1 ? true : false;
            int state = cursor.getInt(cursor.getColumnIndex(SqliteHelper.STATE));
            long totalSize = cursor.getLong(cursor.getColumnIndex(SqliteHelper.TOTAL_SIZE));
            DownloadInfo downloadInfo = new DownloadInfo(url, name, path);
            //downloadInfo.setmIsFinished(isFinished);
            downloadInfo.setmTotalSize(totalSize);
            downloadInfo.setmState(state);
            downloadTasks.put(url, downloadInfo);
            downloadUrls.add(url);
        }
        return (HashMap<String, DownloadInfo>) downloadTasks;
    }


//    public static List<String> getDownloadUrls(Context context){
//        SqliteHelper sqliteHelper = new SqliteHelper(context, DataHelper.DB_NAME, null, SqliteHelper.TB_VERSION);
//        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
//        List<String> downloadUrls = new ArrayList<>();
//        Cursor cursor = sqLiteDatabase.query(SqliteHelper.TB_DOWNLOAD_TASK, new String[]{SqliteHelper.URL},
//                null, null, null, null, null);
//        cursor.moveToFirst();
//        if(!cursor.isAfterLast()){
//            String url =
//        }
//    }
}
