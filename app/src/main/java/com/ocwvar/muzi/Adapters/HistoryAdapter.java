package com.ocwvar.muzi.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetHistoryScrollAtBottomCallback;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Views.RatingBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 品着龙井剥金橘 on 2016/11/9.
 * 预约历史列表
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    public OnClickLitener mOnClickLitener;
    ArrayList<BulletinBean> bulletinBeenList = new ArrayList<>();
    OnGetHistoryScrollAtBottomCallback onGetHistoryScrollAtBottomCallback;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false));
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final BulletinBean bulletinBean = bulletinBeenList.get(position);
            holder.title_text.setText(bulletinBean.getTitle());
            holder.introduce_text.setText(bulletinBean.getIntroduce());
            if(bulletinBean.getUpdate()!=""){
            holder.time_text.setText(getTime(bulletinBean.getUpdate()));
            }else {
                holder.time_text.setVisibility(View.INVISIBLE);
            }

            if(!TextUtils.isEmpty(bulletinBean.getScore())&&bulletinBean.getScore()!=""){
                float score=Float.parseFloat(bulletinBean.getScore());
                holder.ratingBar.setStar(score);
            }else {
                holder.ratingBar.setEnabled(false);
            }
            if(bulletinBean.getOrderStatus()!=-1){
                if(bulletinBean.getOrderStatus()==0){
                    holder.reply_text.setText("取消预约");
                    holder.status_text.setText(setSpan("预约状态：正在受理","预约状态："));
                }else if(bulletinBean.getOrderStatus()==1){
                    holder.reply_text.setText("我要评价");
                    holder.status_text.setText(setSpan("预约状态：已完成","预约状态："));
                }else if(bulletinBean.getOrderStatus()==2){
                    holder.reply_text.setText("查看详细");
                    holder.status_text.setText(setSpan("预约状态：已评价","预约状态："));
                }else if(bulletinBean.getOrderStatus()==3){
                    holder.reply_text.setText("查看详细");
                    holder.status_text.setText(setSpan("预约状态：已忽略","预约状态："));
                }else if(bulletinBean.getOrderStatus()==4){
                    holder.reply_text.setText("查看详细");
                    holder.status_text.setText(setSpan("预约状态：未响应","预约状态："));
                }else {
                    holder.status_text.setText(setSpan("预约状态：获取失败","预约状态："));
                }
            }
        if (bulletinBeenList.size() > 0 && position == bulletinBeenList.size() - 1 && onGetHistoryScrollAtBottomCallback != null) {
            onGetHistoryScrollAtBottomCallback.onHistoryScrollAtBottom();
        }


        if(mOnClickLitener!=null){
                holder.reply_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos=holder.getLayoutPosition();
                        mOnClickLitener.onItemClick(holder.reply_text, pos, bulletinBean);
                    }
                });
            }
    }

    @Override
    public int getItemCount() {
        if(bulletinBeenList==null){
            return 0;
        }else {
            return bulletinBeenList.size();
        }
    }

    public void addData( ArrayList<BulletinBean> list){
        this.bulletinBeenList.addAll(list);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_text;
        TextView introduce_text;
        TextView reply_text;
        TextView status_text;
        TextView time_text;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            title_text= (TextView) itemView.findViewById(R.id.appraise_name);
            introduce_text= (TextView) itemView.findViewById(R.id.appraise_introduce);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar2);
            reply_text= (TextView) itemView.findViewById(R.id.history_date);
            status_text= (TextView) itemView.findViewById(R.id.orderStatus);
            time_text = (TextView) itemView.findViewById(R.id.orderTime);

        }
    }

    public void clearDatas(){
        bulletinBeenList.clear();
        notifyDataSetChanged();
    }

    public interface OnClickLitener
    {
        void onItemClick(View view, int position,BulletinBean bulletinBean);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickLitener(OnClickLitener mOnClickLitener){
        this.mOnClickLitener=mOnClickLitener;

    }
    public void setOnGetHistoryScrollAtBottomCallback(OnGetHistoryScrollAtBottomCallback onGetHistoryScrollAtBottomCallback){
        this.onGetHistoryScrollAtBottomCallback=onGetHistoryScrollAtBottomCallback;
    }

    //给TextView设置前景颜色
    private SpannableStringBuilder setSpan(String str, String spanStr){
        int start=str.indexOf(spanStr);
        int end=start+spanStr.length();
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#fb7e00")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    //转换时间
    private String getTime(String date) {
        Date startDate=new Date(Long.parseLong(date));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=simpleDateFormat.format(startDate);
        return dateStr;
    }

}
