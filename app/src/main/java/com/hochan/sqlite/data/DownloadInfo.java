package com.hochan.sqlite.data;


import java.io.File;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/6/4.
 */
public class DownloadInfo {

    private String mUrl;
    private String mName;
    private String mStoragePath;
    private Future<File> mDownloading;
    private long mTotalSize;
    private long mDownloadedSize;
    private boolean mIsFinished = false;

    public DownloadInfo(String mUrl, String mName, String mStoragePath) {
        this.mUrl = mUrl;
        this.mName = mName;
        this.mStoragePath = mStoragePath;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmStoragePath() {
        return mStoragePath;
    }

    public void setmStoragePath(String mStoragePath) {
        this.mStoragePath = mStoragePath;
    }

    public Future<File> getmDownloading() {
        return mDownloading;
    }

    public void setmDownloading(Future<File> mDownloading) {
        this.mDownloading = mDownloading;
    }

    public long getmTotalSize() {
        return mTotalSize;
    }

    public void setmTotalSize(long mTotalSize) {
        this.mTotalSize = mTotalSize;
    }

    public long getmDownloadedSize() {
        return mDownloadedSize;
    }

    public void setmDownloadedSize(long mDownloadedSize) {
        this.mDownloadedSize = mDownloadedSize;
    }

    public boolean ismIsFinished() {
        return mIsFinished;
    }

    public void setmIsFinished(boolean mIsFinished) {
        this.mIsFinished = mIsFinished;
    }
}
