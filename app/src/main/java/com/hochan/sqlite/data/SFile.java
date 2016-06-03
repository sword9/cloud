package com.hochan.sqlite.data;

/**
 * Created by Administrator on 2016/6/3.
 */
public class SFile {

    private boolean mIsDirectory;
    private String mAbsolutePath;
    private String mName;

    public SFile(boolean mIsDirectory, String mName) {
        this.mIsDirectory = mIsDirectory;
        this.mName = mName;
    }

    public boolean ismIsDirectory() {
        return mIsDirectory;
    }

    public void setmIsDirectory(boolean mIsDirectory) {
        this.mIsDirectory = mIsDirectory;
    }

    public String getmAbsolutePath() {
        return mAbsolutePath;
    }

    public void setmAbsolutePath(String mAbsolutePath) {
        this.mAbsolutePath = mAbsolutePath;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
