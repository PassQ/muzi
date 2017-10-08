package com.ocwvar.muzi.Adapters.RepireTaskAdapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.RepireBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnRepireListScrollAtBottomCallback;
import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.RepireTaskAdapters
 * Data: 2016/7/1 0:03
 * Project: Muzi
 * 已处理列表适配器
 */
public class RepiredAdapter extends RecyclerView.Adapter {

    private ArrayList<RepireBean> repireBeen;
    private OnRecycleViewClickCallbacks.OnClickRepireListCallback onClickRepireListCallback;
    private OnRepireListScrollAtBottomCallback onRepireListScrollAtBottomCallback;

    public RepiredAdapter(OnRecycleViewClickCallbacks.OnClickRepireListCallback onClickRepireListCallback, OnRepireListScrollAtBottomCallback onRepireListScrollAtBottomCallback) {
        repireBeen = new ArrayList<>();
        this.onClickRepireListCallback = onClickRepireListCallback;
        this.onRepireListScrollAtBottomCallback = onRepireListScrollAtBottomCallback;
    }

    /**
     * 往列表中添加数据
     *
     * @param repireBeen 数据列表
     */
    public void addData(ArrayList<RepireBean> repireBeen) {
        this.repireBeen.addAll(repireBeen);
        notifyDataSetChanged();
    }

    /**
     * 清空列表的数据
     */
    public void clear() {
        this.repireBeen.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepiredViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repire, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RepireBean repireBean = repireBeen.get(position);
        RepiredViewHolder itemView = (RepiredViewHolder) holder;

        itemView.preview.setText(repireBean.getPreview());
        itemView.title.setText(repireBean.getTitle());
        switch (repireBean.getCurrentState()) {
            case "1":
                itemView.status.setText("预指定");
                break;
            case "2":
                itemView.status.setText("公开");
                break;
            case "3":
                itemView.status.setText("已受理");
                break;
            case "4":
                itemView.status.setText("待评价");
                break;
            case "5":
                itemView.status.setText("维修结束");
                break;
            case "6":
                itemView.status.setText("强制指派");
                break;
        }

        if (repireBean.getPicturesID() != null && !TextUtils.isEmpty(repireBean.getPicturesThu()[0])) {
            //OCImageLoader.loader().loadImage(repireBean.getPicturesThu()[0],repireBean.getPicturesThu()[0],itemView.cover);
            Picasso
                    .with(itemView.cover.getContext())
                    .load(repireBean.getPicturesThu()[0])
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(itemView.cover);
        } else {
            itemView.cover.setImageDrawable(new ColorDrawable(Color.WHITE));
        }

        if (onRepireListScrollAtBottomCallback != null && position == repireBeen.size() - 1) {
            onRepireListScrollAtBottomCallback.onScrollAtBottom();
        }

    }

    @Override
    public int getItemCount() {
        return repireBeen.size();
    }

    class RepiredViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cover;
        TextView title;
        TextView preview;
        TextView status;

        public RepiredViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.item_repire_image);
            title = (TextView) itemView.findViewById(R.id.item_repire_title);
            preview = (TextView) itemView.findViewById(R.id.item_repire_message);
            status = (TextView) itemView.findViewById(R.id.item_repire_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickRepireListCallback != null && repireBeen.size() > 0 && getAdapterPosition() >= 0 && getAdapterPosition() < repireBeen.size()) {
                onClickRepireListCallback.onClickList(repireBeen.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}
