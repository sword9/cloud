package com.hochan.sqlite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hochan.sqlite.R;
import com.hochan.sqlite.adapter.ShowAdapter;
import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.sql.DataHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class WorkersListFragment extends Fragment implements ShowAdapter.OnAdapterListener{

    public final static String TAG = "list_fragment";

    private View mView;
    private RecyclerView recyShow;
    private ShowAdapter mShowAdapter;
    private Context mContext;
    private List<Worker> mWorkers;
    private LinearLayoutManager mLayoutManager;

    public static WorkersListFragment newInstance(){
        WorkersListFragment workersListFragment = new WorkersListFragment();
        return workersListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_show, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mContext = getContext();

        recyShow = (RecyclerView) mView.findViewById(R.id.recy_show);
        mLayoutManager = new LinearLayoutManager(mContext);
        recyShow.setLayoutManager(mLayoutManager);
        recyShow.setItemAnimator(new DefaultItemAnimator());
        DataHelper dataHelper = new DataHelper(mContext);
        mWorkers = dataHelper.getWorkersInfo();
        mShowAdapter = new ShowAdapter(mContext, mWorkers);
        mShowAdapter.setOnAdapterListener(this);
        recyShow.setAdapter(mShowAdapter);
    }

    public void clear(){
        System.out.println("清空");
        mWorkers.clear();
        mShowAdapter = new ShowAdapter(mContext, null);
        recyShow.setAdapter(mShowAdapter);
        //mLayoutManager.removeAllViews();
        //mShowAdapter.notifyDataSetChanged();
    }

    public void add(Worker worker){
        mWorkers.add(worker);
        mShowAdapter.notifyItemInserted(mWorkers.size() - 1);
        mShowAdapter.notifyItemRangeInserted(mWorkers.size() - 1, mWorkers.size());
    }

    public void showAll(){
        DataHelper dataHelper = new DataHelper(mContext);
        mWorkers = dataHelper.getWorkersInfo();
        mShowAdapter = new ShowAdapter(mContext, mWorkers);
        mShowAdapter.setOnAdapterListener(this);
        recyShow.setAdapter(mShowAdapter);
    }

    public void search(String key, int type){
        DataHelper dataHelper = new DataHelper(mContext);
        List<Worker> results = new ArrayList<>();
        switch (type){
            case SearchDialogFragment.SEARCH_BY_ID:
                try {
                    results = dataHelper.getWorkerByID(Integer.parseInt(key));
                }catch (Exception e){
                    Toast.makeText(mContext, "输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case SearchDialogFragment.SEARCH_BY_NAME:
                results = dataHelper.getWorkersByName(key);
                break;
            case SearchDialogFragment.SEARCH_BY_TOWERNUM:
                results = dataHelper.getWorkersByTowerNumber(key);
                break;
            case SearchDialogFragment.SEARCH_BY_WORHSTATE:
                results = dataHelper.getWorkersByWorkState(key);
                break;
        }
        if(results == null)
            Toast.makeText(mContext, "查询结果为空", Toast.LENGTH_SHORT).show();
        //}else{
            mWorkers = results;
            mShowAdapter = new ShowAdapter(mContext, results);
            mShowAdapter.setOnAdapterListener(this);
            recyShow.setAdapter(mShowAdapter);
        //}
    }

    @Override
    public void longclick(final int position) {
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(EditDialogFragment.DELETEEDIT_DIALOG, mWorkers.get(position).getmName(),
                mWorkers.get(position).getmPhoneNumber(),
                mWorkers.get(position).getmTowerNumber(),
                mWorkers.get(position).getmWorkState());
        editDialogFragment.setOnDialogListener(new EditDialogFragment.OnDialogListener() {
            @Override
            public void clicked(int index, Worker worker) {
                switch (index){
                    case EditDialogFragment.DELETE:
                        DataHelper dataHelper = new DataHelper(mContext);
                        dataHelper.deleteByID(Integer.parseInt(mWorkers.get(position).getmID()));
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        mWorkers.remove(position);
                        //mShowAdapter.notifyDataSetChanged();
                        mShowAdapter.notifyItemRemoved(position);
                        mShowAdapter.notifyItemRangeChanged(position, mWorkers.size());
                        //mShowAdapter.notifyDataSetChanged();
                        break;
                    case EditDialogFragment.EDIT:
                        DataHelper tmpDataHelper = new DataHelper(mContext);
                        tmpDataHelper.updateByID(Integer.parseInt(mWorkers.get(position).getmID()), worker);
                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                        mWorkers.get(position).setmName(worker.getmName());
                        mWorkers.get(position).setmPhoneNumber(worker.getmPhoneNumber());
                        mWorkers.get(position).setmTowerNumber(worker.getmTowerNumber());
                        mWorkers.get(position).setmWorkState(worker.getmWorkState());
                        mShowAdapter.notifyDataSetChanged();
                        break;
                }
                Toast.makeText(mContext, String.valueOf(index), Toast.LENGTH_SHORT).show();
            }
        });
        editDialogFragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "dialog");

    }
}
