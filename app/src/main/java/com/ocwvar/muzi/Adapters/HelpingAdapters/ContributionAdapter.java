package com.ocwvar.muzi.Adapters.HelpingAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnContrScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.HelpingAdapters
 * Date: 2016/6/13  13:07
 * Project: Muzi
 * 公益展示列表适配器
 */
public class ContributionAdapter extends RecyclerView.Adapter {

    private ArrayList<ArtclesBean> artclesBeen;
    private OnContrScrollAtBottomCallback scrollAtBottomCallback;
    private OnRecycleViewClickCallbacks.OnContributionListClickCallback onContributionListClickCallback;

    public ContributionAdapter() {
        artclesBeen = new ArrayList<>();
    }

    /**
     * 添加新的 "公益展示" 信息
     *
     * @param artclesBeen 信息Bean  List
     */
    public void addContributionBean(ArrayList<ArtclesBean> artclesBeen) {
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

    public void setScrollAtBottomCallback(OnContrScrollAtBottomCallback scrollAtBottomCallback) {
        this.scrollAtBottomCallback = scrollAtBottomCallback;
    }

    public void setOnHelpingListClickCallback(OnRecycleViewClickCallbacks.OnContributionListClickCallback onContributionListClickCallback) {
        this.onContributionListClickCallback = onContributionListClickCallback;
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

        viewHolder.image.setVisibility(View.GONE);

        if (artclesBeen.size() > 0 && position == artclesBeen.size() - 1 && scrollAtBottomCallback != null) {
            scrollAtBottomCallback.onContrScrollAtBottom();
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
            if (onContributionListClickCallback != null && artclesBeen.size() > 0 && getAdapterPosition() >= 0 && getAdapterPosition() < artclesBeen.size()) {
                onContributionListClickCallback.onContributionClick(artclesBeen.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}
