package com.ocwvar.muzi.Adapters.HXSoundAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnSuggestScrollAtBottomCallback;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.HelpingAdapters
 * Date: 2016/7/7  13:07
 * Project: Muzi
 * 合理化建议 列表适配器
 */
public class SuggestAdapter extends RecyclerView.Adapter {

    private ArrayList<ArtclesBean> artclesBeen;
    private OnSuggestScrollAtBottomCallback scrollAtBottomCallback;
    private OnRecycleViewClickCallbacks.OnSuggestListClickCallback onSuggestListClickCallback;

    public SuggestAdapter() {
        artclesBeen = new ArrayList<>();
    }

    /**
     * 添加新的 "合理化建议" 信息
     *
     * @param artclesBeen 信息Bean  List
     */
    public void addSuggestBean(ArrayList<ArtclesBean> artclesBeen) {
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

    public void setScrollAtBottomCallback(OnSuggestScrollAtBottomCallback scrollAtBottomCallback) {
        this.scrollAtBottomCallback = scrollAtBottomCallback;
    }

    public void setOnSuggestListClickCallback(OnRecycleViewClickCallbacks.OnSuggestListClickCallback onSuggestListClickCallback) {
        this.onSuggestListClickCallback = onSuggestListClickCallback;
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
            scrollAtBottomCallback.onSuggestScrollAtBottom();
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
            if (onSuggestListClickCallback != null && artclesBeen.size() > 0 && getAdapterPosition() >= 0 && getAdapterPosition() < artclesBeen.size()) {
                onSuggestListClickCallback.onSuggestListClick(artclesBeen.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}
