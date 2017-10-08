package com.ocwvar.muzi.Adapters.ServiceArtcleAdapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnServicePeopleScrollAtBottomCallback;
import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.HelpingAdapters
 * Date: 2016/7/7  13:07
 * Project: Muzi
 * 服务投诉 列表适配器
 */
public class ServicePeopleAdapter extends RecyclerView.Adapter {

    private ArrayList<ArtclesBean> artclesBeen;
    private OnServicePeopleScrollAtBottomCallback scrollAtBottomCallback;
    private OnRecycleViewClickCallbacks.OnServicePeopleListClickCallback onServicePeopleListClickCallback;

    public ServicePeopleAdapter() {
        artclesBeen = new ArrayList<>();
    }

    /**
     * 添加新的 "服务求助" 信息
     *
     * @param artclesBeen 信息Bean  List
     */
    public void addServicePeopleBean(ArrayList<ArtclesBean> artclesBeen) {
        if (artclesBeen != null && artclesBeen.size() > 0) {
            this.artclesBeen.addAll(artclesBeen);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空数据
     */
    public void clearDatas() {
        this.artclesBeen.clear();
    }

    public void setScrollAtBottomCallback(OnServicePeopleScrollAtBottomCallback scrollAtBottomCallback) {
        this.scrollAtBottomCallback = scrollAtBottomCallback;
    }

    public void setOnServicePeopleListClickCallback(OnRecycleViewClickCallbacks.OnServicePeopleListClickCallback onServicePeopleListClickCallback) {
        this.onServicePeopleListClickCallback = onServicePeopleListClickCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HelpaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helpask, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HelpaskViewHolder viewHolder = (HelpaskViewHolder) holder;
        ArtclesBean artclesBean = artclesBeen.get(position);
        viewHolder.title.setText(artclesBean.getTitle());
        viewHolder.message.setText(artclesBean.getPreview());

        if (artclesBean.getRelativePath() != null) {
            viewHolder.image.setVisibility(View.VISIBLE);
            //OCImageLoader.loader().loadImage(artclesBean.getRelativePath(),artclesBean.getRelativePath(),viewHolder.image);
            Picasso
                    .with(viewHolder.image.getContext())
                    .load(artclesBean.getRelativeThuPath())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(viewHolder.image);
        } else {
            viewHolder.image.setVisibility(View.GONE);
        }

        if (artclesBeen.size() > 0 && position == artclesBeen.size() - 1 && scrollAtBottomCallback != null) {
            scrollAtBottomCallback.onServicePeopleScrollAtBottom();
        }

    }

    @Override
    public int getItemCount() {
        return artclesBeen.size();
    }

    class HelpaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, message;
        ImageView image;

        public HelpaskViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_helpask_title);
            message = (TextView) itemView.findViewById(R.id.item_helpask_message);
            image = (ImageView) itemView.findViewById(R.id.item_repire_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onServicePeopleListClickCallback != null && artclesBeen.size() > 0 && getAdapterPosition() >= 0 && getAdapterPosition() < artclesBeen.size()) {
                onServicePeopleListClickCallback.onServicePeopleListClick(artclesBeen.get(getAdapterPosition()), getAdapterPosition());
            } else {
                Log.e("onClick", "onClick ");
            }
        }
    }

}
