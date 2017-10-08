package com.ocwvar.muzi.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.SlidingMenuBean;
import com.ocwvar.muzi.Beans.UserType;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/5  22:15
 * Project: Muzi
 * 侧滑菜单列表Adapter
 */

public class SlidingMenuAdapter extends RecyclerView.Adapter {

    private ArrayList<SlidingMenuBean> beanArrayList;
    private OnRecycleViewClickCallbacks.OnSlidingMenuClickCallback onSlidingMenuClickCallback;
    private Resources resources;

    public SlidingMenuAdapter(Resources resources) {
        this.resources = resources;
        beanArrayList = new ArrayList<>();
        init();
    }


    /**
     * 添加默认选项
     */
    private void init() {
        if(AppOptions.USERINFO.user.getUserType() != UserType.GUEST){
            beanArrayList.add(new SlidingMenuBean(R.drawable.ic_sign, "每日签到"));
        }

        beanArrayList.add(new SlidingMenuBean(R.drawable.ic_home, AppOptions.ApplicationContext.getString(R.string.switch_community_title)));

        if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {

            beanArrayList.add(new SlidingMenuBean(R.drawable.ic_user, AppOptions.ApplicationContext.getString(R.string.userinfo_title)));
            beanArrayList.add(new SlidingMenuBean(R.drawable.ic_lock, AppOptions.ApplicationContext.getString(R.string.changePassword_title)));
        }
        beanArrayList.add(new SlidingMenuBean(R.drawable.ic_settings, AppOptions.ApplicationContext.getString(R.string.setting_title)));
        if (AppOptions.USERINFO.user.getUserType() == UserType.SERVICE_MAN) {
            beanArrayList.add(new SlidingMenuBean(R.drawable.ic_repire_task, "维修列表"));
        }

    }


    /**
     * 点击回调
     *
     * @param onSlidingMenuClickCallback 回调接口
     */
    public void setOnSlidingMenuClickCallback(OnRecycleViewClickCallbacks.OnSlidingMenuClickCallback onSlidingMenuClickCallback) {
        this.onSlidingMenuClickCallback = onSlidingMenuClickCallback;
    }



    /**
     * 更新侧滑菜单选项
     *
     * @param bean 选项的Bean
     */
    public void updateData(SlidingMenuBean bean) {
        beanArrayList.add(bean);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sliding_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        SlidingMenuBean bean = beanArrayList.get(position);
        if (bean != null) {
            viewHolder.title.setText(bean.getTitle());
            viewHolder.icon.setImageDrawable(resources.getDrawable(bean.getIconRes()));
        }
    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textView_slidingmenu_title);
            icon = (ImageView) itemView.findViewById(R.id.imageView_slidingmenu_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onSlidingMenuClickCallback != null) {
                onSlidingMenuClickCallback.onSlidingMenuClick(getAdapterPosition());
            }
        }
    }
}
