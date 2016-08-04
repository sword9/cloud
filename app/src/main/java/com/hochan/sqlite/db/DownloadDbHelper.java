package com.hochan.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DownloadDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static DownloadDbHelper helper = null;

    private static final String SQL_CREATE =
            "create table thread_info(" +
                    "_id integer primary key autoincrement," +
                    "thread_id integer," +
                    "filename text,"+
                    "start long," +
                    "end long," +
                    "upload int,"+
                    "finished long)";

    private static final String SQL_DROP = "drop table if exists thread_info";
    private static final int VERSION = 1;

    public static DownloadDbHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DownloadDbHelper(context);
        }
        return helper;
    }

    private DownloadDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
