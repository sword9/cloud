package com.hochan.multi_file_selector.data;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AudioFile extends File {

    final public static Uri SARTWORKURI = Uri
            .parse("content://media/external/audio/albumart");

    private Uri mAlbumUri;
    private long mAlbumId;
    private long mDuration;
    private String mArtist;

    public AudioFile(int mType, String mName, String mPath, String mDataAdded, long mSzie) {
        super(mType, mName, mPath, mDataAdded, mSzie);
    }

    public Uri getmAlbumUri() {
        return mAlbumUri;
    }

    public void setmAlbumUri(Uri mAlbumUri) {
        this.mAlbumUri = mAlbumUri;
    }

    public long getmAlbumId() {
        return mAlbumId;
    }

    public void setmAlbumId(long mAlbumId) {
        this.mAlbumId = mAlbumId;
        this.mAlbumUri = ContentUris.withAppendedId(SARTWORKURI, mAlbumId);
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }
}
