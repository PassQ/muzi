package com.ocwvar.muzi.Adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.ImageHelper.OCImageLoader;
import com.squareup.picasso.Picasso;


/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Data: 2016/7/6 2:12
 * Project: Muzi
 * 维修详情内图片加载的适配器
 */
public class RepireDerailPicturesAdapter extends RecyclerView.Adapter {

    String[] pics;

    public RepireDerailPicturesAdapter(String[] pics) {
        this.pics = pics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleImageView(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_imageview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SimpleImageView itemView = (SimpleImageView) holder;
        String url = pics[position];
        if (!TextUtils.isEmpty(url)) {
            Picasso
                    .with(itemView.imageView.getContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(itemView.imageView);
        } else {
            itemView.imageView.setImageDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    @Override
    public int getItemCount() {
        if (pics == null) {
            return 0;
        } else {
            return pics.length;
        }
    }

    private class SimpleImageView extends RecyclerView.ViewHolder {

        ImageView imageView;

        SimpleImageView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.simple_ImageView);
        }
    }
}
