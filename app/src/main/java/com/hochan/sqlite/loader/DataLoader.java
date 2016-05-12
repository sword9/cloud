package com.hochan.sqlite.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hochan.sqlite.data.MediaFile;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/7.
 */
public class DataLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID };

    private final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION};

    private final String[] AUDIO_PROJECTION = {
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION};

    private Context mContext;
    private int mType;

    public DataLoader(Context context, int type){
        this.mContext = mContext;
        this.mType = type;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (mType){
            case MediaFile.TYPE_IMAGE:
                CursorLoader acursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        //?对应后面的selectionArgs用于转义特殊字符
                        IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? ",
                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                return acursorLoader;
            case MediaFile.TYPE_VIDEO:
                CursorLoader bcursorLoader = new CursorLoader(mContext,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        VIDEO_PROJECTION,
                        IMAGE_PROJECTION[4]+">0 AND ",
                        null, IMAGE_PROJECTION[2]+" DESC");
                return bcursorLoader;
            case MediaFile.TYPE_AUDIO:
                //CursorLoader ccursorLoader = new
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null)
            if(data.getCount() > 0) {
                switch (mType) {
                    case MediaFile.TYPE_IMAGE:
                        handleImageData(data);
                        break;
                    case MediaFile.TYPE_AUDIO:
                        handleAudioData(data);
                        break;
                    case MediaFile.TYPE_VIDEO:
                        handleVideoData(data);
                        break;
                }
            }
    }

    private void handleVideoData(Cursor data) {
        ArrayList<MediaFile> videoFiles = new ArrayList<>();
        data.moveToFirst();
        do{
            String path = data.getString(
                    data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
            String name = data.getString(
                    data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
            String dateAdded = data.getString(
                    data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
            String size = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));
            MediaFile videoFile = new MediaFile(
                    MediaFile.TYPE_VIDEO, name, path, dateAdded, size);
            videoFile.setmDuration(
                    data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[6])));
            videoFiles.add(videoFile);
            mCallBack.finish(videoFiles);
        }while (data.moveToNext());

    }

    private void handleAudioData(Cursor data) {
    }

    private void handleImageData(Cursor data) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface DataLoaderCallBack{
        public void finish(ArrayList<MediaFile> mediaFiles);
    }

    private DataLoaderCallBack mCallBack;

    public void setCallBack(DataLoaderCallBack callBack){
        this.mCallBack = callBack;
    }
}
