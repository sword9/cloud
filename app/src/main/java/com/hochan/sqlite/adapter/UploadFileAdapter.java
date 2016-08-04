package com.hochan.sqlite.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.service.DownloadService2;
import com.hochan.sqlite.service.UploadService2;

import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class UploadFileAdapter extends BaseAdapter{
    private Context context;
    private List<FileInfo> fileLists;
    private LayoutInflater inflater;

    public UploadFileAdapter(Context context, List<FileInfo> fileLists) {
        this.context = context;
        this.fileLists = fileLists;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileLists.size();
    }

    @Override
    public Object getItem(int position) {
        return fileLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo info = fileLists.get(position);
        final ViewHolder holder;
       //if (convertView == null) {
            convertView = inflater.inflate(R.layout.fileupload, null);
            holder= new ViewHolder();
            holder.name =(TextView) convertView.findViewById(R.id.upload_name);
            holder.proText =(TextView) convertView.findViewById(R.id.upload_pro_text);
            holder.start =(Button) convertView.findViewById(R.id.upload_start);
            holder.pause =(Button) convertView.findViewById(R.id.upload_pause);
            holder.progressBar =(ProgressBar) convertView.findViewById(R.id.upload_progressBar);

            holder.name.setText(info.getFileName());

            if (info.getFileName() == "") {
                holder.name.setText("/");
            }

        /*    convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/

        //如果是文件夹，则隐藏按钮和进度条
        if (info.isDir()) {
            holder.start.setVisibility(View.INVISIBLE);
            holder.pause.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.INVISIBLE);
        }

        int pro= (int) info.getFinish();
        holder.progressBar.setProgress(pro);
        holder.proText.setText(new StringBuffer().append(pro).append("%"));
        holder.start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.proText.setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, UploadService2.class);
                intent.setAction(UploadService2.ACTION_START);
                intent.putExtra("fileinfo", info);
                context.startService(intent);
            }
        });

        holder.pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadService2.class);
                intent.setAction(UploadService2.ACTION_PAUSE);
                intent.putExtra("fileinfo", info);
                context.startService(intent);

            }
        });
        //notifyDataSetChanged();
        return convertView;
    }

    /**
     * 更新列表进度条
     */
    public void updateProgress(int id, long progress){
        FileInfo fileInfo = fileLists.get(id);
        fileInfo.setFinish(progress);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView name;
        TextView proText;
        Button start;
        Button pause;
        ProgressBar progressBar;
    }
}
