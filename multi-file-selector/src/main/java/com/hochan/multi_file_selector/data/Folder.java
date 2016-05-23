package com.hochan.multi_file_selector.data;

import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Folder {

    private String mName;
    private String mPath;
    private List<File> mFiles;
    private int mMediaType;
    private Uri mFolderCover;

    public Folder(int mediaType, String mName, String mPath, List<File> mFiles) {
        this.mMediaType = mediaType;
        this.mName = mName;
        this.mPath = mPath;
        this.mFiles = mFiles;
        switch (mMediaType){
            case File.TYPE_IMAGE:
                mFolderCover = Uri.fromFile(new java.io.File(mFiles.get(0).getmPath()));
                break;
            case File.TYPE_AUDIO:
                mFolderCover = ((AudioFile) mFiles.get(0)).getmAlbumUri();
                break;
            case File.TYPE_VIDEO:
                String thumbnailPath = ((VideoFile)mFiles.get(0)).getmThumbnailPath();
                if(!TextUtils.isEmpty(thumbnailPath)){
                    java.io.File file = new java.io.File(thumbnailPath);
                    mFolderCover = Uri.fromFile(file);
                }
                break;
        }
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

    public List<File> getmFiles() {
        return mFiles;
    }

    public void setmFiles(List<File> mFiles) {
        this.mFiles = mFiles;
    }

    public Uri getmFolderCover() {
        return mFolderCover;
    }

    public void setmFolderCover(Uri mFolderCover) {
        this.mFolderCover = mFolderCover;
    }

    public int getmMediaType() {
        return mMediaType;
    }
}
