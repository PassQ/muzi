package com.ocwvar.muzi.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.FuncGLBean;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/4  9:59
 * Project: Muzi
 * 主界面宫格列表Adapter
 */

public class FuncGLAdapter extends RecyclerView.Adapter {

    private ArrayList<FuncGLBean> funcGLData;
    private OnRecycleViewClickCallbacks.OnFuncGLClickCallback onFuncGLClickCallback;
    private Resources resources;

    public FuncGLAdapter(Resources resources) {
        this.resources = resources;
        funcGLData = new ArrayList<>();
        addDefaultData();
    }

    /**
     * 点击回调
     *
     * @param onFuncGLClickCallback 回调接口
     */
    public void setOnFuncGLClickCallback(OnRecycleViewClickCallbacks.OnFuncGLClickCallback onFuncGLClickCallback) {
        this.onFuncGLClickCallback = onFuncGLClickCallback;
    }

    /**
     * 添加默认选项数据
     */
    private void addDefaultData() {

        putData(new FuncGLBean(R.drawable.lcon_notice, "公告通知"));
        putData(new FuncGLBean(R.drawable.lcon_culture, "居家文化"));
        putData(new FuncGLBean(R.drawable.lcon_guide, "办事指南"));
        putData(new FuncGLBean(R.drawable.lcon_help, "爱心互助"));
        putData(new FuncGLBean(R.drawable.lcon_payment, "一键缴费"));
        putData(new FuncGLBean(R.drawable.lcon_repair, "物业报修"));
        putData(new FuncGLBean(R.drawable.lcon_harmonious, "和谐之声"));
        putData(new FuncGLBean(R.drawable.lcon_service, "服务动态"));

    }

    /**
     * 添加数据
     *
     * @param bean 要添加的Bean
     */
    public void putData(FuncGLBean bean) {
        if (isAvailable(bean)) {
            funcGLData.add(bean);
        }
        notifyDataSetChanged();
    }

    /**
     * 检查添加的Bean的有效性
     *
     * @param bean 欲添加的Bean
     * @return 有效性
     */
    private boolean isAvailable(FuncGLBean bean) {
        if (bean != null) {
            if (bean.getIconResId() != -1 && !TextUtils.isEmpty(bean.getTitle())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuncItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_func, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FuncItemViewHolder viewHolder = (FuncItemViewHolder) holder;
        FuncGLBean bean = funcGLData.get(position);
        if (bean != null) {
            viewHolder.title.setText(bean.getTitle());
            viewHolder.icon.setImageDrawable(resources.getDrawable(bean.getIconResId()));
        }
    }

    @Override
    public int getItemCount() {
        return funcGLData.size();
    }

    class FuncItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView title;

        public FuncItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.func_item_icon);
            title = (TextView) itemView.findViewById(R.id.func_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onFuncGLClickCallback != null) {
                onFuncGLClickCallback.onFuncGLClick(getAdapterPosition());
            }
        }

    }
}
