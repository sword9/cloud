package com.hochan.sqlite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.sqlite.R;
import com.hochan.sqlite.adapter.FolderAdapter;
import com.hochan.sqlite.data.FolderContainer;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.cache.FileResource;

/**
 * Created by Administrator on 2016/6/2.
 */
public class FileFragment extends Fragment{

    private RecyclerView recyFileList;
    private Context mContext;
    private FolderAdapter mFolderAdapter;

    private String mCurentPath;

    public static FileFragment newInstance(){
        FileFragment fileFragment = new FileFragment();
        return  fileFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyFileList = (RecyclerView) view.findViewById(R.id.recy_file_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        mFolderAdapter = new FolderAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyFileList.setLayoutManager(layoutManager);
        recyFileList.setAdapter(mFolderAdapter);

        File file = new File("/play");
        if(file.isDirectory()){

            System.out.print("是文件夹");
        }else{
            System.out.println("是文件");
        }
    }
}
