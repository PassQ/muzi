package com.ocwvar.muzi.Adapters.BulletinAdapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnCircumScrollAtBottomCallback;
import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 覃毅 on 2016/11/14.
 * 周边信息适配器
 */

public class BulletinCircumAdapter extends RecyclerView.Adapter<BulletinCircumAdapter.MyViewHolder> {
    public ArrayList<BulletinBean> bulletinBeenList=new ArrayList<>();
    public BulletinCircumAdapter.OnClickLitener mOnClickLitener;
    OnCircumScrollAtBottomCallback onCircumScrollAtBottomCallback;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circum, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BulletinBean bulletinBean=bulletinBeenList.get(position);
        if(!TextUtils.isEmpty(bulletinBean.getImagePath())) {
            Picasso
                    .with(holder.image.getContext())
                    .load(bulletinBean.getImagePath())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.image);
        }else {
            Picasso
                    .with(holder.image.getContext())
                    .load(R.drawable.lmg_periphery)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.image);
        }

        holder.title_text.setText(bulletinBean.getTitle());
        holder.introduce_text.setText(setSpan("服务介绍："+bulletinBean.getIntroduce(),"服务介绍："));
        holder.tel_text.setText(setSpan("联系方式："+bulletinBean.getPhoneNum(),"联系方式："));
        if(!TextUtils.isEmpty(bulletinBean.getUpdate())){
            holder.time_text.setText("发布时间："+getTime(bulletinBean.getUpdate()));
        }else {
            holder.time_text.setText("");
        }

        if (bulletinBeenList.size() > 0 && position == bulletinBeenList.size() - 1 && onCircumScrollAtBottomCallback != null) {
            onCircumScrollAtBottomCallback.onCircumScrollAtBottom();
        }

        if (mOnClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnClickLitener.onItemClick(holder.itemView, pos,bulletinBean);
                }
            });
        }


    }


    //转换时间
    private String getTime(String date) {
        Date startDate=new Date(Long.parseLong(date));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=simpleDateFormat.format(startDate);
        return dateStr;
    }

    @Override
    public int getItemCount() {
        if (bulletinBeenList!= null) {
            return bulletinBeenList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_text;
        TextView time_text;
        TextView introduce_text;
        TextView tel_text;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title_text = (TextView) itemView.findViewById(R.id.item_circum_title);
            time_text = (TextView) itemView.findViewById(R.id.item_circum_time);
            introduce_text = (TextView) itemView.findViewById(R.id.item_circum_message);
            tel_text= (TextView) itemView.findViewById(R.id.item_circum_tel);
            image= (ImageView) itemView.findViewById(R.id.item_circum_image);
        }


    }
    public void addData(ArrayList<BulletinBean>list){
        this.bulletinBeenList.addAll(list);
        notifyDataSetChanged();
    }
    public void setData(ArrayList<BulletinBean>list){
        bulletinBeenList.clear();
        this.bulletinBeenList.addAll(list);
        notifyDataSetChanged();
    }
    //清空数据操作
    public void clearDatas(){
        bulletinBeenList.clear();
        notifyDataSetChanged();
    }

    public interface OnClickLitener
    {
        void onItemClick(View view, int position,BulletinBean bulletinBean);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickLitener(BulletinCircumAdapter.OnClickLitener mOnClickLitener){
        this.mOnClickLitener=mOnClickLitener;

    }
    public void setOnCircumScrollAtBottomCallback(OnCircumScrollAtBottomCallback onCircumScrollAtBottomCallback){
        this.onCircumScrollAtBottomCallback=onCircumScrollAtBottomCallback;
    }

    //字体前景颜色
    private SpannableStringBuilder setSpan(String str,String spanStr){
        int start=str.indexOf(spanStr);
        int end=start+spanStr.length();
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#fb7e00")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

}
