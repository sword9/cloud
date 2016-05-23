package com.hochan.multi_file_selector.data;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/10.
 */
public class File {

    public final static int TYPE_IMAGE = 0;
    public final static int TYPE_AUDIO = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_MEDIANONE = 3;
    public final static int TYPE_ALL = 4;

    public final static ArrayList<String> TYPE_NAME = new ArrayList<>();

    static {
        TYPE_NAME.add(TYPE_IMAGE, "图片");
        TYPE_NAME.add(TYPE_AUDIO, "音频");
        TYPE_NAME.add(TYPE_VIDEO, "视频");
        TYPE_NAME.add(TYPE_MEDIANONE, "文档");
        TYPE_NAME.add(TYPE_ALL, "文件");
    }

    private int mType;
    private String mName;
    private String mPath;
    private String mDataAdded;
    private long mSize;

    public File(int mType, String mName, String mPath, String mDataAdded, long mSzie) {
        this.mType = mType;
        this.mName = mName;
        this.mPath = mPath;
        this.mDataAdded = mDataAdded;
        this.mSize = mSzie;
    }

    @Override
    public boolean equals(Object o){
        try {
            File file = (File) o;
            return this.mPath.equals(file.mPath);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public String getmDataAdded() {
        return mDataAdded;
    }

    public void setmDataAdded(String mDataAdded) {
        this.mDataAdded = mDataAdded;
    }

}
