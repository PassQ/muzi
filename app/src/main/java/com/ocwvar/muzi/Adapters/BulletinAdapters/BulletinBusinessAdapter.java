package com.ocwvar.muzi.Adapters.BulletinAdapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnBusinessScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 品着龙井剥金橘 on 2016/11/11.
 * 商家列表适配器
 */

public class BulletinBusinessAdapter extends RecyclerView.Adapter<BulletinBusinessAdapter.MyViewHolder>{


    ArrayList<BulletinBean> bulletinBeenList = new ArrayList<>();
    public OnClickLitener mOnClickLitener;


    public OnBusinessScrollAtBottomCallback onBusinessScrollAtBottomCallback;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bulletin,parent,false));
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final BulletinBean bulletinBean=bulletinBeenList.get(position);

        if(!TextUtils.isEmpty(bulletinBean.getImagePath())){
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
                    .load(R.drawable.lmg_facade)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.image);
        }
        
        holder.title_text.setText(bulletinBean.getTitle());
        holder.score_text.setText("综合评分："+bulletinBean.getScore());
        holder.introduce_text.setText(bulletinBean.getTitle());
        if(bulletinBean.getStatus()==1){
            holder.status_text.setText("正在营业");
            holder.status_text.setTextColor(Color.parseColor("#fb7e00"));

        }else if(bulletinBean.getStatus()==2){
            holder.status_text.setText("暂停营业");
            holder.status_text.setTextColor(Color.GRAY);
        }else {
            holder.status_text.setText("");
        }


        if (bulletinBeenList.size() > 0 && position == bulletinBeenList.size() - 1 && onBusinessScrollAtBottomCallback != null) {
            onBusinessScrollAtBottomCallback.onBusinessScrollAtBottom();
        }


        if(mOnClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos= holder.getLayoutPosition();
                    mOnClickLitener.onItemClick(holder.itemView,pos,bulletinBean);
                }
            });
        }

    }



    @Override
    public int getItemCount() {
        if(bulletinBeenList!=null){
            return bulletinBeenList.size();
        }else {
            return 0;
        }

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title_text;
        TextView introduce_text;
        TextView score_text;
        TextView status_text;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title_text= (TextView) itemView.findViewById(R.id.item_bulletin_title);
            introduce_text= (TextView) itemView.findViewById(R.id.item_bulletin_message);
            score_text= (TextView) itemView.findViewById(R.id.item_bulletin_score);
            status_text= (TextView) itemView.findViewById(R.id.item_bulletin_status);
            image= (ImageView) itemView.findViewById(R.id.item_bulletin_image);
        }

    }

    //回调监听接口
    public interface OnClickLitener
    {
        void onItemClick(View view, int position,BulletinBean bulletinBean);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickLitener(BulletinBusinessAdapter.OnClickLitener mOnClickLitener){
        this.mOnClickLitener=mOnClickLitener;

    }

    //添加列表数据
    public void addData( ArrayList<BulletinBean> list){
        this.bulletinBeenList.addAll(list);
        notifyDataSetChanged();
    }

    //设置列表数据（清空操作）
    public void setData( ArrayList<BulletinBean> list ){
        this.bulletinBeenList.clear();
        this.bulletinBeenList.addAll(list);
        notifyDataSetChanged();
    }
    public void clearDatas(){
        this.bulletinBeenList.clear();
        notifyDataSetChanged();
    }

    public void setOnBusinessScrollAtBottomCallback(OnBusinessScrollAtBottomCallback onBusinessScrollAtBottomCallback) {
        this.onBusinessScrollAtBottomCallback = onBusinessScrollAtBottomCallback;
    }

}
