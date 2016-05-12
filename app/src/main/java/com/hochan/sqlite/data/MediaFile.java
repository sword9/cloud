package com.hochan.sqlite.data;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MediaFile {

    public final static int TYPE_IMAGE = 0;
    public final static int TYPE_VIDEO = 1;
    public final static int TYPE_AUDIO = 2;

    private int mType;
    private String mName;
    private String mPath;
    private String mDataAdded;
    private String mSize;
    private String mArtist;
    private String mDuration;

    public MediaFile(int mType, String mName, String mPath, String mDataAdded, String mSzie) {
        this.mType = mType;
        this.mName = mName;
        this.mPath = mPath;
        this.mDataAdded = mDataAdded;
        this.mSize = mSzie;
    }

    @Override
    public boolean equals(Object o){
        try {
            MediaFile mediaFile = (MediaFile) o;
            return this.mPath.equals(mediaFile.mPath);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
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

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public String getmDataAdded() {
        return mDataAdded;
    }

    public void setmDataAdded(String mDataAdded) {
        this.mDataAdded = mDataAdded;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }
}
