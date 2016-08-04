package com.hochan.sqlite.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static int TB_VERSION = 3;

    public final static String TB_NAME = "worker";
    public final static String TB_DOWNLOAD_TASK = "download_task";

    public final static String ID = "_id";
    //public final static String NAME = "姓名";
    //public final static String PHONE_NUMBER = "电话";
    //public final static String TOWER_NUMBER = "电塔编号";
    //public final static String WORK_STATE = "工作状况";
    public final static String TIME_STAMP = "时间戳";
    public static final String NICKNAME = "昵称";
    public static final String PASSWORD = "密码";
    public static final String WORK_STATE = "工作状态";

    public static final String URL = "url";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String STATE = "state";
    public static final String TOTAL_SIZE = "total_size";
    public static final String DOWNLOADED_SIZE = "downloaded_size";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //在调getReadableDatabase或getWritableDatabase时，会判断指定的数据库是否存在，
    //不存在则调SQLiteDatabase.create创建， onCreate只在数据库第一次创建时才执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表worker
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" +
                ID + " varchar primary key," +
                NICKNAME + " varchar," +
                PASSWORD + " varchar," +
                //NAME + " varchar," +
                //PHONE_NUMBER + " varchar," +
                //TOWER_NUMBER + " varchar," +
                WORK_STATE + " varchar," +
                TIME_STAMP + " timestamp NOT NULL" +
                ")");

        //创建表download_task
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_DOWNLOAD_TASK + "(" +
                ID + " varchar primary key," +
                URL + " varchar," +
                NAME + " varchar," +
                PATH + " varchar," +
                STATE + " int," +
                TOTAL_SIZE + " bigint," +
                DOWNLOADED_SIZE + " bigint" +
                ")");
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.TB_NAME);
        onCreate(db);
    }
}
