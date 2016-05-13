package com.hochan.sqlite.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public final static String TB_NAME = "worker";
    public final static String ID = "_id";
    //public final static String NAME = "姓名";
    //public final static String PHONE_NUMBER = "电话";
    //public final static String TOWER_NUMBER = "电塔编号";
    //public final static String WORK_STATE = "工作状况";
    public final static String TIME_STAMP = "时间戳";
    public static final String NICKNAME = "昵称";
    public static final String PASSWORD = "密码";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" +
                ID + " varchar primary key," +
                NICKNAME + " varchar," +
                PASSWORD + " varchar," +
                //NAME + " varchar," +
                //PHONE_NUMBER + " varchar," +
                //TOWER_NUMBER + " varchar," +
                //WORK_STATE + " varchar," +
                TIME_STAMP + " timestamp NOT NULL" +
                ")");
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.TB_NAME);
        onCreate(db);
    }
}
