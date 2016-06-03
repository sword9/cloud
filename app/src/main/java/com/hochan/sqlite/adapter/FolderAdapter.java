package com.hochan.sqlite.adapter;

import android.content.Context;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hochan.multi_file_selector.data.File;
import com.hochan.sqlite.data.FolderContainer;
import com.hochan.sqlite.data.SFile;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Administrator on 2016/6/3.
 */
public class FolderAdapter extends RecyclerView.Adapter{

    private String mCurentParentPath;
    private Context mContext;

    private CircularArray<SFile> mSFiles = new CircularArray<>();

    public FolderAdapter(Context context){
        this.mContext = context;
        getFileList("/");
        mCurentParentPath = "/";
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
        return mSFiles.size();
    }

    private void getFileList(String path){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", "666");
            jsonObject.put("path", path);
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
                            }else{
                                System.out.println("Directory为空");
                            }

                            if(folderContainer.getFile() != null){
                                System.out.println(folderContainer.getFile().size());
                                List<String> files = folderContainer.getFile();
                                for(String file : files){
                                    System.out.println(file);
                                }
                            }else{
                                System.out.println("File为空");
                            }
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

    class FolderViewHolder extends RecyclerView.ViewHolder {

        public FolderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
