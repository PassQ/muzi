package com.ocwvar.muzi.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyImageCallback;
import com.ocwvar.muzi.Network.Callbacks.OnReplyListAtBottomCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Views.RatingBar;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 品着龙井剥金橘 on 2016/11/11.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>  {
    public OnClickLitener mOnClickLitener;
    ArrayList<BulletinBean> itemList= new ArrayList<>();
    OnReplyListAtBottomCallback onReplyListAtBottomCallback;
    Context context;


    public ReviewAdapter(Context context){
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appraise,parent,false));

        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BulletinBean bulletinBean=itemList.get(position);
        holder.setIsRecyclable(false);

        holder.title_text.setText(bulletinBean.getTitle());
        holder.time_text.setText(getTime(bulletinBean.getUpdate()));
        holder.content_text.setText(bulletinBean.getIntroduce());
        Log.e( "onBindViewHolder ", bulletinBean.getScore());
        holder.ratingBar.setStar(Float.parseFloat(bulletinBean.getScore()));
        if(bulletinBean.getImageList()!=null){
            if(bulletinBean.getImageList().size()>0){
                for (int i = 0; i < bulletinBean.getImageList().size(); i++) {
                    final ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(160, 160);
                    layoutParams.setMargins(20,0,20,0);

                    imageView.setLayoutParams(layoutParams);
                    imageView.refreshDrawableState();
                    Picasso
                            .with(imageView.getContext())
                            .load(bulletinBean.getImageList().get(i).getImagePath())
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.ic_failed)
                            .placeholder(R.drawable.ic_loading)
                            .into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setId(i);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnClickLitener.onImageClick(bulletinBean.getImageList().get(imageView.getId()).getImagePath());
                        }
                    });
                    holder.imageLayout.addView(imageView);
                }
            }
        }



        if (itemList.size() > 0 && position == itemList.size() - 1 && onReplyListAtBottomCallback != null) {
            onReplyListAtBottomCallback.onReplyListAtBottom();
        }

        if(mOnClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=holder.getLayoutPosition();
                    mOnClickLitener.onItemClick(holder.itemView,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(itemList==null){
            return 0;
        }else {
            return itemList.size();
        }
    }

    public void addData( ArrayList<BulletinBean> item){
        this.itemList.addAll(item);
        notifyDataSetChanged();
    }


    public void clearDatas(){

        this.itemList.clear();
        notifyDataSetChanged();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_text;
        TextView content_text;
        TextView time_text;
        RatingBar ratingBar;
        LinearLayout imageLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title_text= (TextView) itemView.findViewById(R.id.appraise_name);
            content_text= (TextView) itemView.findViewById(R.id.appraise_introduce);
            time_text= (TextView) itemView.findViewById(R.id.appraise_date);
            ratingBar= (RatingBar) itemView.findViewById(R.id.ratingBar);
            imageLayout = (LinearLayout) itemView.findViewById(R.id.imageLayout);
        }
    }

    public interface OnClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void onImageClick(String path);
    }

    public void setOnClickLitener(OnClickLitener mOnClickLitener){
        this.mOnClickLitener=mOnClickLitener;
    }

    public void setOnReplyListAtBottomCallback(OnReplyListAtBottomCallback onReplyListAtBottomCallback) {
        this.onReplyListAtBottomCallback = onReplyListAtBottomCallback;
    }


    //转换时间
    private String getTime(String date) {
        Date startDate=new Date(Long.parseLong(date));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateStr=simpleDateFormat.format(startDate);
        return dateStr;
    }
}