package com.ocwvar.muzi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.InfoListBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/4  10:00
 * Project: Muzi
 * 主界面信息列表
 */

public class InfoListAdapter extends RecyclerView.Adapter {

    private ArrayList<InfoListBean> infoListData;
    private OnRecycleViewClickCallbacks.OnInfoListClickCallback onInfoListClickCallback;

    public InfoListAdapter() {
        this.infoListData = new ArrayList<>();
        initDatas();
    }

    /**
     * 加载默认的信息
     */
    private void initDatas() {
        //putData(new InfoListBean(Color.argb(255,239,91,37),"论坛热门", DataCacher.FORUM_URL, "点击浏览社区论坛 !"));
    }

    /**
     * 点击回调
     *
     * @param onInfoListClickCallback 回调接口
     */
    public void setOnInfoListClickCallback(OnRecycleViewClickCallbacks.OnInfoListClickCallback onInfoListClickCallback) {
        this.onInfoListClickCallback = onInfoListClickCallback;
    }

    /**
     * 添加数据
     *
     * @param bean 要添加的Bean
     */
    public void putData(InfoListBean bean) {
        if (isAvailable(bean) && !infoListData.contains(bean)) {
            infoListData.add(0, bean);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取指定位置的文章信息Bean
     *
     * @param position 指定的位置
     * @return 文章Bean
     */
    public InfoListBean getSelectItem(int position) {
        return infoListData.get(position);
    }

    /**
     * 检查添加的Bean的有效性
     *
     * @param bean 欲添加的Bean
     * @return 有效性
     */
    private boolean isAvailable(InfoListBean bean) {
        if (bean != null) {
            if (!TextUtils.isEmpty(bean.getType()) && !TextUtils.isEmpty(bean.getTitle())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InfoItemViewHolder viewHolder = (InfoItemViewHolder) holder;
        InfoListBean bean = infoListData.get(position);
        if (bean != null) {
//            viewHolder.title.setText(bean.getTitle());
            viewHolder.title.setText("欢迎使用安和物业APP，我们是您最无憾的选择！用安和物业APP，物业缴费，一键报修，让您的生活更方便！");
            viewHolder.type.setText(bean.getType());
            viewHolder.type.setBackgroundColor(bean.getTypeColor());
        }
    }

    @Override
    public int getItemCount() {
        return infoListData.size();
    }

    class InfoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView type;
        TextView title;

        public InfoItemViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.info_item_type);
            title = (TextView) itemView.findViewById(R.id.info_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onInfoListClickCallback != null) {
                onInfoListClickCallback.onInfoListClick(getAdapterPosition());
            }
        }
    }
}
