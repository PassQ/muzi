package com.ocwvar.muzi.Adapters;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnViewPagerClickCallback;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/5  15:53
 * Project: Muzi
 * 主界面滚动显示Adapter
 */

public class MainScrollingVPAdapter extends PagerAdapter implements View.OnClickListener {

    private final int MAX_SCROLLING_COUNT = 5;
    private OnViewPagerClickCallback onViewPagerClickCallback;

    private ArrayList<ArtclesBean> cachedImageURLs;
    private ArrayList<ImageView> views;

    public MainScrollingVPAdapter() {
        cachedImageURLs = new ArrayList<>();
        views = new ArrayList<>();

        //初始化往里面填充NULL的View
        for (int i = 0; i < MAX_SCROLLING_COUNT; i++) {
            views.add(null);
        }

        updateDatas();
    }

    /**
     * 设置点击监听接口
     *
     * @param onViewPagerClickCallback 点击监听接口
     */
    public void setOnViewPagerClickCallback(OnViewPagerClickCallback onViewPagerClickCallback) {
        this.onViewPagerClickCallback = onViewPagerClickCallback;
    }

    /**
     * 获取文章Bean
     *
     * @param position 要获取的位置
     * @return 文章Bean
     */
    public ArtclesBean getItem(int position) {
        return cachedImageURLs.get(position);
    }

    /**
     * 当前的数据是否有效
     *
     * @return 是否有效
     */
    public boolean isHasNULLData() {
        return cachedImageURLs.contains(null);
    }

    /**
     * 更新数据
     */
    public void updateDatas() {
        cachedImageURLs.clear();
        cachedImageURLs.addAll(AppOptions.CACHE.getMainScrollingItems());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cachedImageURLs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (views.get(position) == null) {
            ImageView imageView;
            imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setTag(cachedImageURLs.get(position).getRelativePath());
            views.set(position, imageView);
        }

        ImageView imageView = views.get(position);
        imageView.setOnClickListener(this);
        //OCImageLoader.loader().loadImage(imageView.getTag().toString(),cachedImageURLs.get(position).getRelativePath(),imageView);
        Picasso
                .with(container.getContext())
//                .load(cachedImageURLs.get(position).getRelativePath())
                .load("http://tupian.enterdesk.com/2013/lxy/12/24/3/3.jpg")
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.ic_failed)
                .placeholder(R.drawable.ic_loading)
                .into(imageView);

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = views.get(position);
        if (imageView != null) {
            container.removeView(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        if (onViewPagerClickCallback != null) {
            int position = views.indexOf(v);
            onViewPagerClickCallback.onClickViewPager(cachedImageURLs.get(position));
        }
    }
}
