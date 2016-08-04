package com.hochan.sqlite.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import com.hochan.multi_file_selector.data.File;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.sqlite.R;
import com.hochan.sqlite.data.FolderContainer;
import com.hochan.sqlite.data.SFile;
import com.hochan.sqlite.service.DownloadService;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.SystemDefaultCredentialsProvider;

/**
 * Created by Administrator on 2016/6/3.
 */

//文件下载界面fragment

public class FolderAdapter extends RecyclerView.Adapter{

    private String mCurentParentPath;
    private Context mContext;

    private List<String> mUrls = new ArrayList<>();

    private CircularArray<SFile> mSFiles = new CircularArray<>();

    public FolderAdapter(Context context){
        this.mContext = context;
        mCurentParentPath = "/";
        mUrls.add(new String("http://sw.bos.baidu.com/sw-search-sp/software/8d9eac0aba7/BaiduYunGuanjia_5.4.4.1.exe"));
        mUrls.add(new String("http://sw.bos.baidu.com/sw-search-sp/software/1302ab08845/Evernote_6.1.2.2292.exe"));
        mUrls.add(new String("https://sm.wdjcdn.com/release/files/jupiter/5.16.0.12022/wandoujia-web_seo_baidu_homepage.apk"));
        mUrls.add(new String("http://dlsw.baidu.com/sw-search-sp/soft/3f/12289/Weibo.4.5.3.37575common_wbupdate.1423811415.exe"));
        //getFileList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FolderViewHolder viewHolder = (FolderViewHolder) holder;
        //SFile sFile = mSFiles.get(position);
        viewHolder.tvName.setText(mUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
        //return mSFiles.size();
    }

    //访问服务器获取文件目录
    private void getFileList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", "666");
            jsonObject.put("path", mCurentParentPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
        SQLHttpClient.post(mContext, SQLHttpClient.GET_FILE, stringEntity, RequestParams.APPLICATION_JSON,
                new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println(response);
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            FolderContainer folderContainer = objectMapper.readValue(response.toString(), FolderContainer.class);
                            if(folderContainer.getDirectory() != null){
                                System.out.println(folderContainer.getDirectory().size());
                                List<String> directories = folderContainer.getDirectory();
                                for(String directory : directories){
                                    System.out.println(directory);
                                    SFile sFile = new SFile(true, directory);
                                    sFile.setmAbsolutePath(mCurentParentPath+"/"+directory);
                                    mSFiles.addLast(sFile);
                                }
                            }else{
                                System.out.println("Directory为空");
                            }

                            if(folderContainer.getFile() != null){
                                System.out.println(folderContainer.getFile().size());
                                List<String> files = folderContainer.getFile();
                                for(String file : files){
                                    System.out.println(file);
                                    SFile sFile = new SFile(false, file);
                                    sFile.setmAbsolutePath(mCurentParentPath+"/"+file);
                                    mSFiles.addLast(sFile);
                                }
                            }else{
                                System.out.println("File为空");
                            }

                            notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        System.out.println(responseString);
                    }
                });
    }

    class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvName;

        public FolderViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(DownloadService.URL, mUrls.get(getPosition()));
            mContext.startService(intent);
            Toast.makeText(mContext, "已添加至下载", Toast.LENGTH_SHORT).show();
//            SFile sFile = mSFiles.get(getPosition());
//            if(sFile.ismIsDirectory()){
//                mCurentParentPath = sFile.getmAbsolutePath();
//                getFileList();
//            }else{
//
//            }
        }
    }
}
