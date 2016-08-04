package com.hochan.sqlite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.FileInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class fileExploreAdapter extends RecyclerView.Adapter<fileExploreAdapter.ViewHolder> implements View.OnClickListener{



    private List<FileInfo> data;
    private Context context;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;


    public fileExploreAdapter(List<FileInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public fileExploreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo fileModel = data.get(position);
        if(fileModel.isDir()){
            holder.iv.setImageResource(R.drawable.cloudfolder);
        } else{
            holder.iv.setImageResource(R.drawable.cloudfile);
        }
        holder.tv.setText(fileModel.getFileName());
        holder.itemView.setTag(data.get(position).getFileName());
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_pic);
            tv = (TextView) itemView.findViewById(R.id.tv_filename);
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, String data);
    }
}
