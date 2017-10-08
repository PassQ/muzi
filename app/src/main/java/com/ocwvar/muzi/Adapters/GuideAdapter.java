package com.ocwvar.muzi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/12  15:33
 * Project: Muzi
 * 办事指南列表Adapter
 */
public class GuideAdapter extends RecyclerView.Adapter {

    private ArrayList<ArtclesBean> beanArrayList;
    private OnRecycleViewClickCallbacks.OnGuideListClickCallback onGuideListClick;

    public GuideAdapter() {
        beanArrayList = new ArrayList<>();
    }

    public void setOnGuideListClick(OnRecycleViewClickCallbacks.OnGuideListClickCallback onGuideListClick) {
        this.onGuideListClick = onGuideListClick;
    }

    /**
     * 设置列表数据 (会清空旧的数据)
     *
     * @param beanArrayList 数据源
     */
    public void setArtcles(ArrayList<ArtclesBean> beanArrayList) {
        if (beanArrayList != null) {
            this.beanArrayList.clear();
            this.beanArrayList.addAll(beanArrayList);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加列表数据到尾端
     *
     * @param beanArrayList 数据源
     */
    public void addArtcles(ArrayList<ArtclesBean> beanArrayList) {
        if (beanArrayList != null) {
            this.beanArrayList.addAll(beanArrayList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GuideViewHolder viewHolder = (GuideViewHolder) holder;
        ArtclesBean bean = beanArrayList.get(position);
        viewHolder.title.setText(bean.getTitle());
    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

    class GuideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        public GuideViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textView_guide_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onGuideListClick != null) {
                onGuideListClick.onGuideListClick(getAdapterPosition(), beanArrayList.get(getAdapterPosition()));
            }
        }
    }

}
