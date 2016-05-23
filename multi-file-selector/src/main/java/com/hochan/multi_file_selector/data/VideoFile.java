package com.hochan.multi_file_selector.data;

import android.net.Uri;

/**
 * Created by Administrator on 2016/5/19.
 */
public class VideoFile extends File {

    private long mDuration;
    private Uri mCoverUri;
    private String mThumbnailPath;

    public VideoFile(int mType, String mName, String mPath, String mDataAdded, long mSzie) {
        super(mType, mName, mPath, mDataAdded, mSzie);
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public Uri getmCoverUri() {
        return mCoverUri;
    }

    public void setmCoverUri(Uri mCoverUri) {
        this.mCoverUri = mCoverUri;
    }

    public String getmThumbnailPath() {
        return mThumbnailPath;
    }

    public void setmThumbnailPath(String mThumbnailPath) {
        this.mThumbnailPath = mThumbnailPath;
    }
}
