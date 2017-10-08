package com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters
 * Date: 2016/5/11  20:48
 * Project: Muzi
 * 安和文化园Adapter
 */
public class AnnCulturalParkAdapter extends RecyclerView.Adapter {
    private final String TAG = "安和文化园";

    private ArrayList<ArtclesBean> beanArrayList;
    private OnRecycleViewClickCallbacks.OnCommunityCultureListCallback onCommunityCultureListCallback;
    private OnRecycleViewClickCallbacks.OnScrollAtBottomCallback onScrollAtBottomCallback;

    public AnnCulturalParkAdapter() {
        beanArrayList = new ArrayList<>();
    }

    /**
     * 设置列表点击事件
     *
     * @param onCommunityCultureListCallback 点击事件回调
     */
    public void setOnCommunityCultureListCallback(OnRecycleViewClickCallbacks.OnCommunityCultureListCallback onCommunityCultureListCallback) {
        this.onCommunityCultureListCallback = onCommunityCultureListCallback;
    }

    /**
     * 设置列表滚动到底部时的回调事件
     *
     * @param onScrollAtBottomCallback 事件的回调
     */
    public void setOnScrollAtBottomCallback(OnRecycleViewClickCallbacks.OnScrollAtBottomCallback onScrollAtBottomCallback) {
        this.onScrollAtBottomCallback = onScrollAtBottomCallback;
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
        return new ArtclesItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artcle, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArtclesItemViewHolder viewHolder = (ArtclesItemViewHolder) holder;
        ArtclesBean artclesBean = beanArrayList.get(position);

        if (!TextUtils.isEmpty(artclesBean.getRelativeThuPath())) {
            //OCImageLoader.loader().loadImage(artclesBean.getRelativeThuPath()+position,artclesBean.getRelativeThuPath(),viewHolder.imageView);
            Picasso
                    .with(viewHolder.imageView.getContext())
                    .load(artclesBean.getRelativeThuPath())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageBitmap(null);
        }

        viewHolder.message.setText(artclesBean.getPreview());
        viewHolder.title.setText(artclesBean.getTitle());

        //当滚动到列表的最下方时
        if (position == beanArrayList.size() - 1 && onScrollAtBottomCallback != null) {
            onScrollAtBottomCallback.onScrollAtBottomCallback(TAG);
        }
    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

    /**
     * 每一项的ViewHolder
     */
    public class ArtclesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView message;
        ImageView imageView;

        public ArtclesItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_repire_title);
            message = (TextView) itemView.findViewById(R.id.item_artcleList_message);
            imageView = (ImageView) itemView.findViewById(R.id.item_repire_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onCommunityCultureListCallback != null) {
                onCommunityCultureListCallback.onCommunityCultureListClick(TAG, getAdapterPosition(), beanArrayList.get(getAdapterPosition()));
            }
        }

    }

}
