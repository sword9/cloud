package com.hochan.sqlite.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hochan.sqlite.R;
import com.hochan.sqlite.UI.FileUI;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.service.DownloadService;
import com.hochan.sqlite.service.DownloadService2;
import com.hochan.sqlite.service.UploadService2;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * 多线程下载Adapter
 * Created by Administrator on 2016/7/18.
 */
public class FileListAdapter extends BaseAdapter {
    private Context context;
    private List<FileInfo> fileLists;
    private LayoutInflater inflater;

    public FileListAdapter(Context context, List<FileInfo> fileLists) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FileInfo info = fileLists.get(position);

        final ViewHolder holder;
        //if (convertView == null) {
            convertView = inflater.inflate(R.layout.filedown, null);
            holder= new ViewHolder();
            holder.name =(TextView) convertView.findViewById(R.id.name);
            holder.proText =(TextView) convertView.findViewById(R.id.pro_text);
            holder.start =(Button) convertView.findViewById(R.id.start);
            holder.pause =(Button) convertView.findViewById(R.id.pause);
            holder.progressBar =(ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.delete = (Button) convertView.findViewById(R.id.delete);
            holder.name.setText(info.getFileName());

            if (info.getFileName() == "") {
                holder.name.setText("/");
                holder.delete.setVisibility(View.INVISIBLE);
            }

        /*    convertView.setTag(holder);
        } else {
          holder = (ViewHolder) convertView.getTag();
        }*/


        //如果是文件夹，则隐藏按钮和进度条
        if (info.isDir()) {
            holder.start.setVisibility(View.INVISIBLE);
            holder.pause.setVisibility(View.INVISIBLE);
            //holder.delete.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.INVISIBLE);
        }

        int pro= (int) info.getFinish();
        holder.progressBar.setProgress(pro);
        holder.proText.setText(new StringBuffer().append(pro).append("%"));
        holder.start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.proText.setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, DownloadService2.class);
                intent.setAction(DownloadService2.ACTION_START);
                intent.putExtra("fileinfo", info);
                context.startService(intent);
            }
        });

        holder.pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DownloadService2.class);
                intent.setAction(DownloadService2.ACTION_PAUSE);
                intent.putExtra("fileinfo", info);
                context.startService(intent);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!info.isDir()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userId","123456");
                        jsonObject.put("path",FileUI.localDir);
                        jsonObject.put("fileName",info.getFileName());
                        StringEntity stringEntity = new StringEntity(jsonObject.toString());
                        SQLHttpClient.post(context.getApplicationContext(), SQLHttpClient.BASE_URL+"deleteFile",stringEntity, RequestParams.APPLICATION_JSON,
                                new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                        fileLists.remove(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);

                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userId","123456");
                        jsonObject.put("path",FileUI.localDir+info.getFileName());
                        StringEntity stringEntity = new StringEntity(jsonObject.toString());
                        SQLHttpClient.post(context.getApplicationContext(), SQLHttpClient.BASE_URL+"deleteDirectory",stringEntity, RequestParams.APPLICATION_JSON,
                                new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                        fileLists.remove(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);

                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        notifyDataSetChanged();
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
        Button delete;
        ProgressBar progressBar;
    }
}
