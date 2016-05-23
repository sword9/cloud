package com.hochan.multi_file_selector.tool;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import com.hochan.multi_file_selector.view.SquaredImageView;

/**
 * Created by Administrator on 2016/5/21.
 */
public class GetVideoThumbnail extends AsyncTask<String, Void, Bitmap>{

    private SquaredImageView mSiv;
    private String mTag;

    public GetVideoThumbnail(SquaredImageView siv, String tag){
        this.mSiv = siv;
        this.mTag = tag;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap =  ThumbnailUtils.createVideoThumbnail(
                params[0], MediaStore.Video.Thumbnails.MICRO_KIND);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(mSiv != null){
            if(mSiv.getVisibility() == View.VISIBLE && mSiv.getTag().equals(mTag))
                mSiv.setImageBitmap(bitmap);
        }
    }
}
