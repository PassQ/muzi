package com.ocwvar.muzi.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.SimpleDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 品着龙井剥金橘 on 2016/11/14.
 * 商家详情图片列表适配器
 */

public class BusinessImageAdapter extends RecyclerView.Adapter<BusinessImageAdapter.MyViewHolder> {
    public OnClickLitener mOnClickLitener;
    ArrayList<BusinessImageBean> imageList =new ArrayList<>();
    private int total =1;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(imageList.size()>0) {
            BusinessImageBean businessImageBean = imageList.get(position);
            if (!TextUtils.isEmpty(businessImageBean.getImagePath()) && businessImageBean.getImagePath() != "") {
                Picasso
                        .with(holder.imageView.getContext())
                        .load(businessImageBean.getImagePath())
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.ic_failed)
                        .placeholder(R.drawable.ic_loading)
                        .into(holder.imageView);
                          holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
                Picasso
                        .with(holder.imageView.getContext())
                        .load(R.drawable.lmg_facade_preview)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.ic_failed)
                        .placeholder(R.drawable.ic_loading)
                        .into(holder.imageView);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }else {
            Picasso
                    .with(holder.imageView.getContext())
                    .load(R.drawable.lmg_periphery_preview)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.imageView);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        if(mOnClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(imageList.size()>0) {
                        int pos = holder.getLayoutPosition();
                        mOnClickLitener.onItemClick(holder.itemView, pos, imageList.get(pos).getImagePath());
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return total;

    }
    public class MyViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
             imageView = (ImageView) itemView.findViewById(R.id.item_imageView);
        }

    }

    //添加数据
    public void addData(ArrayList<BusinessImageBean> list){
        imageList.addAll(list);
        notifyDataSetChanged();
    }

    //清空数据
    public void clearDatas(){

        this.imageList.clear();

        notifyDataSetChanged();
    }

    public interface OnClickLitener
    {
        void onItemClick(View view, int position, String path);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickLitener(BusinessImageAdapter.OnClickLitener mOnClickLitener){
        this.mOnClickLitener=mOnClickLitener;

    }
}
