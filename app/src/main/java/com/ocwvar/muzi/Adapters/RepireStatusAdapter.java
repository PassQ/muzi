package com.ocwvar.muzi.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.RepireStatusBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/10  10:17
 * Project: Muzi
 * 报修状态列表Adapter
 */

public class RepireStatusAdapter extends RecyclerView.Adapter {

    private ArrayList<RepireStatusBean> repireStatusBeen;
    private OnRecycleViewClickCallbacks.OnRepireStatusListCallback onRepireStatusListCallback;

    public RepireStatusAdapter() {
        repireStatusBeen = new ArrayList<>();
    }

    public void updateDatas(ArrayList<RepireStatusBean> repireStatusBeen) {
        this.repireStatusBeen.clear();
        this.repireStatusBeen.addAll(repireStatusBeen);
        notifyDataSetChanged();
    }

    public void setOnRepireStatusListCallback(OnRecycleViewClickCallbacks.OnRepireStatusListCallback onRepireStatusListCallback) {
        this.onRepireStatusListCallback = onRepireStatusListCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepireStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repire_status, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RepireStatusViewHolder statusViewHolder = (RepireStatusViewHolder) holder;
        RepireStatusBean repireStatusBean = repireStatusBeen.get(position);
        try {
            statusViewHolder.time.setText(repireStatusBean.getSubmitTime().split(" ")[0]);
        } catch (Exception e) {
            statusViewHolder.time.setText(repireStatusBean.getSubmitTime());
        }
        statusViewHolder.title.setText(repireStatusBean.getTitle());

        switch (repireStatusBean.getCurrentState()) {
            case "-1":
                statusViewHolder.status.setText("待审核");
                statusViewHolder.status.setTextColor(Color.argb(255, 255, 0, 0));
                break;
            case "0":
                statusViewHolder.status.setText("待分配");
                statusViewHolder.status.setTextColor(Color.argb(255, 255, 0, 0));
                break;
            case "1":
                statusViewHolder.status.setText("预指定");
                statusViewHolder.status.setTextColor(Color.argb(255, 255, 0, 0));
                break;
            case "2":
                statusViewHolder.status.setText("待受理");
                statusViewHolder.status.setTextColor(Color.argb(255, 255, 242, 0));
                break;
            case "3":
                statusViewHolder.status.setText("已受理");
                statusViewHolder.status.setTextColor(Color.argb(255, 30, 255, 0));
                break;
            case "4":
                statusViewHolder.status.setText("待评价");
                statusViewHolder.status.setTextColor(Color.argb(255, 30, 255, 0));
                break;
            case "5":
                statusViewHolder.status.setText("维修完成");
                statusViewHolder.status.setTextColor(Color.argb(255, 30, 255, 0));
                break;
            case "6":
                statusViewHolder.status.setText("已指定");
                statusViewHolder.status.setTextColor(Color.argb(255, 255, 0, 0));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return repireStatusBeen.size();
    }

    class RepireStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time, title, status;

        public RepireStatusViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.textView_repire2_time);
            title = (TextView) itemView.findViewById(R.id.textView_repire2_title);
            status = (TextView) itemView.findViewById(R.id.textView_repire2_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onRepireStatusListCallback != null) {
                onRepireStatusListCallback.onRepireStatusMenuClick(repireStatusBeen.get(getAdapterPosition()));
            }
        }

    }
}
