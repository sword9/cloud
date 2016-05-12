package com.hochan.sqlite.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.MediaFile;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/10.
 */
public class VideoFileAdapter extends RecyclerView.Adapter{

    private ArrayList<MediaFile> mMediaFiles;
    private Context mContext;

    public VideoFileAdapter(Context context, ArrayList<MediaFile> mediaFiles){
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        public TextView tvVideoInfo;

        public VideoViewHolder(View itemView) {
            super(itemView);
            tvVideoInfo = (TextView) itemView.findViewById(R.id.tv_video_info);
        }
    }
}
