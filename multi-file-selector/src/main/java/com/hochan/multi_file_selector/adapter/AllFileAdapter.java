package com.hochan.multi_file_selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.listener.MediaFileAdapterListener;
import com.hochan.multi_file_selector.view.SquaredImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */

//上传文件中的所有文件
public class AllFileAdapter extends RecyclerView.Adapter{

    private File[] mFiles = new File[]{new File("/storage/sdcard0"), new File("/storage/sdcard1")};    //sdcard0和sdcard1
    private List<File> mSelectedFiled = new ArrayList<>();  //已选择文件
    private Context mContext;
    private MediaFileAdapterListener mAdapterListener;

    public AllFileAdapter(Context context){
        //File file = new File("/");
        //mFiles = file.listFiles();
        System.out.println(mFiles.length);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_linear_item, parent, false);
        return new AllFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AllFileViewHolder viewHolder = (AllFileViewHolder) holder;
        File file = mFiles[position];
        String name = file.getName();
        viewHolder.tvName.setText(name);
        if(mSelectedFiled.contains(file)){
            viewHolder.ivCheck.setImageResource(R.drawable.checkbox_checked);
        }else{
            viewHolder.ivCheck.setImageResource(R.drawable.checkbox_unchecked);
        }
        if(file.isDirectory()){
            viewHolder.sivIcon.setImageResource(R.drawable.icon_list_folder);
            viewHolder.ivCheck.setVisibility(View.INVISIBLE);
            //System.out.println("是文件夹");
        }else{
            viewHolder.ivCheck.setVisibility(View.VISIBLE);
            if(name.endsWith(".mp3"))
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_audiofile);
            else if(name.endsWith(".jpg") || name.endsWith(".png"))
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_image);
            else if(name.endsWith(".rar") || name.endsWith(".zip"))
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_compressfile);
            else if(name.endsWith(".apk"))
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_apk);
            else if(name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mkv"))
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_videofile);
            else if(name.endsWith(".pdf"))
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_pdf);
            else
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_unknown);
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.length;
    }

    class AllFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SquaredImageView sivIcon;
        private ImageView ivCheck;
        private TextView tvName, tvHidden;

        public AllFileViewHolder(View itemView) {
            super(itemView);
            sivIcon = (SquaredImageView) itemView.findViewById(R.id.siv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvHidden = (TextView) itemView.findViewById(R.id.tv_duration);
            tvHidden.setVisibility(View.GONE);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mFiles[getPosition()].isDirectory()){
                mAdapterListener.recordFolder(mFiles[getPosition()]);
                mFiles = mFiles[getPosition()].listFiles();
                notifyDataSetChanged();
            }else{
                if(mSelectedFiled.contains(mFiles[getPosition()])){
                    mSelectedFiled.remove(mFiles[getPosition()]);
                    ivCheck.setImageResource(R.drawable.checkbox_unchecked);
                }else{
                    mSelectedFiled.add(mFiles[getPosition()]);
                    ivCheck.setImageResource(R.drawable.checkbox_checked);
                }
            }
            mAdapterListener.fileSelected(mSelectedFiled.size());
        }
    }

    public void setmAdapterListener(MediaFileAdapterListener mAdapterListener) {
        this.mAdapterListener = mAdapterListener;
        File file = new File("", "手机");
        mAdapterListener.recordFolder(file);
    }

    public List<File> getmSelectedFiled() {
        return mSelectedFiled;
    }

    public void updateFolder(File file){
        if(file.getName().endsWith("手机")){
            mFiles = new File[]{new File("/storage/sdcard0"), new File("/storage/sdcard1")};
            notifyDataSetChanged();
            return;
        }
        mFiles = file.listFiles();
        notifyDataSetChanged();
    }

}
