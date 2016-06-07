package com.hochan.sqlite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.hochan.multi_file_selector.adapter.ImageAdapter;
import com.hochan.sqlite.R;
import com.hochan.sqlite.data.DownloadInfo;
import com.hochan.sqlite.tools.Tool;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 */
public class DownloadTaskAdapter extends RecyclerView.Adapter{

    private static final String TAG = "DownloadTaskAdapter";

    private Context mContext;
    private List<String> mDownloadUrls = new ArrayList<>();
    private HashMap<String, DownloadInfo> mDownloadTasks = new HashMap<>();
    private DownloadInfo mExpandItem;
    private int mExpandPositon = 0;

    public DownloadTaskAdapter(Context context){
        this.mContext = context;
    }

    public void setDownloadTasks(List<String> downloadUrls, HashMap<String, DownloadInfo> downloadTaskList){
        this.mDownloadUrls = downloadUrls;
        this.mDownloadTasks = downloadTaskList;
        System.out.println(TAG+":"+"mDownloadUrls.size():"+mDownloadUrls.size());
        System.out.println(TAG+":"+"mDownloadTasks.size():"+mDownloadTasks.size());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_task, parent, false);
        return new DownloadTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DownloadTaskViewHolder viewHolder = (DownloadTaskViewHolder) holder;
        DownloadInfo downloadInfo = mDownloadTasks.get(mDownloadUrls.get(position));
        viewHolder.tvName.setText(downloadInfo.getmName());
        viewHolder.tvUrl.setText(downloadInfo.getmUrl());
        viewHolder.tvSize.setText(Tool.getSizeFormat(downloadInfo.getmTotalSize()));
        viewHolder.mProgressBar.setMax((int) downloadInfo.getmTotalSize());
        viewHolder.mProgressBar.setProgress((int) downloadInfo.getmDownloadedSize());
        if(mDownloadTasks.get(mDownloadUrls.get(position)).equals(mExpandItem)){
            viewHolder.llControl.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llControl.setVisibility(View.GONE);
        }
        switch (downloadInfo.getmState()){
            case DownloadInfo.STATE_LOADING:
                viewHolder.tvFinish.setVisibility(View.GONE);
                viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                viewHolder.tvUrl.setText(downloadInfo.getmUrl());
                viewHolder.btnCancle.setText("取消");
                break;
            case DownloadInfo.STATE_FINISHED:
                viewHolder.mProgressBar.setVisibility(View.GONE);
                viewHolder.tvFinish.setVisibility(View.VISIBLE);
                viewHolder.tvUrl.setText(downloadInfo.getmStoragePath());
                viewHolder.btnCancle.setText("删除");
                break;
            case DownloadInfo.STATE_UNFINISHED:

                break;
        }
        if(downloadInfo.getmDownloadedSize() == downloadInfo.getmTotalSize()){
            viewHolder.mProgressBar.setVisibility(View.GONE);
            viewHolder.tvFinish.setVisibility(View.VISIBLE);
            viewHolder.tvUrl.setText(downloadInfo.getmStoragePath());
            viewHolder.btnCancle.setText("删除");
        }else{
            viewHolder.tvFinish.setVisibility(View.GONE);
            viewHolder.mProgressBar.setVisibility(View.VISIBLE);
            viewHolder.tvUrl.setText(downloadInfo.getmUrl());
            viewHolder.btnCancle.setText("取消");
        }
    }

    @Override
    public int getItemCount() {
        return mDownloadUrls.size();
    }

    class DownloadTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivIcon;
        public TextView tvName, tvUrl, tvSize, tvFinish;
        public ProgressBar mProgressBar;
        public LinearLayout llDownloadTaskInfo, llControl;
        public Button btnRestart, btnCancle, btnDelete;

        public DownloadTaskViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvSize = (TextView) itemView.findViewById(R.id.tv_size);
            tvUrl = (TextView) itemView.findViewById(R.id.tv_url);
            tvFinish = (TextView) itemView.findViewById(R.id.tv_finish);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress);

            llDownloadTaskInfo = (LinearLayout) itemView.findViewById(R.id.ll_download_task_info);
            llControl = (LinearLayout) itemView.findViewById(R.id.ll_control);

            btnRestart = (Button) itemView.findViewById(R.id.btn_restart);
            btnCancle = (Button) itemView.findViewById(R.id.btn_cancle);

            llDownloadTaskInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_download_task_info:
                    if(mDownloadTasks.get(mDownloadUrls.get(getPosition())).equals(mExpandItem))
                        mExpandItem = null;
                    else
                        mExpandItem = mDownloadTasks.get(mDownloadUrls.get(getPosition()));
                    notifyItemChanged(getPosition());
                    notifyItemChanged(mExpandPositon);
                    mExpandPositon = getPosition();
                    break;
                case R.id.btn_restart:
                    break;
                case R.id.btn_cancle:
                    DownloadInfo downloadInfo = mDownloadTasks.get(mDownloadUrls.get(getPosition()));
                    if(downloadInfo.getmTotalSize() == downloadInfo.getmDownloadedSize()){
                        File file = new File(downloadInfo.getmStoragePath());
                        if(file.exists())
                            file.delete();
                    }
                    break;
            }
        }
    }
}
