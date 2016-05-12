package com.hochan.sqlite.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hochan.sqlite.data.MediaFile;
import com.hochan.sqlite.loader.DataLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/10.
 */
public class GetVideoFragment extends Fragment implements DataLoader.DataLoaderCallBack{

    private DataLoader mVideoDataLoader =
            new DataLoader(getActivity(), MediaFile.TYPE_VIDEO);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVideoDataLoader.setCallBack(this);
        getActivity().getSupportLoaderManager().initLoader(MediaFile.TYPE_VIDEO,
                null, mVideoDataLoader);
    }

    @Override
    public void finish(ArrayList<MediaFile> mediaFiles) {
        if(mediaFiles != null){
            for(MediaFile mediaFile : mediaFiles){

            }
        }
    }
}
