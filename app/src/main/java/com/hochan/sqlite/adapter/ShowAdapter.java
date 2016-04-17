package com.hochan.sqlite.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.Worker;

import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ShowAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<Worker> mWorkers;

    public ShowAdapter(Context mContext, List<Worker> mWorkers) {
        this.mContext = mContext;
        this.mWorkers = mWorkers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShowViewHolder showViewHolder = (ShowViewHolder) holder;
        showViewHolder.tvId.setText(String.valueOf(mWorkers.get(position).getmID()));
        showViewHolder.tvName.setText(mWorkers.get(position).getmName());
        showViewHolder.tvPhoneNumber.setText(mWorkers.get(position).getmPhoneNumber());
        showViewHolder.tvTowerNumber.setText(mWorkers.get(position).getmTowerNumber());
        showViewHolder.tvWorkState.setText(mWorkers.get(position).getmWorkState());
    }

    @Override
    public int getItemCount() {
        if(mWorkers == null){
            return 0;
        }
        return mWorkers.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvId, tvName, tvPhoneNumber, tvTowerNumber, tvWorkState;
        public LinearLayout llItemBackground;

        public ShowViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phone_number);
            tvTowerNumber = (TextView) itemView.findViewById(R.id.tv_tower_number);
            tvWorkState = (TextView) itemView.findViewById(R.id.tv_work_state);
            llItemBackground = (LinearLayout) itemView.findViewById(R.id.ll_item_background);
            llItemBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mAdapterListener != null){
                mAdapterListener.longclick(getPosition());
            }
        }
    }

    private OnAdapterListener mAdapterListener;

    public void setOnAdapterListener(OnAdapterListener adapterListener){
        this.mAdapterListener = adapterListener;
    }

    public interface OnAdapterListener{
        public void longclick(int position);
    }
}
