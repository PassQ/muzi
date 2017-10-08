package com.ocwvar.muzi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.PayRecordBean;
import com.ocwvar.muzi.Network.Callbacks.OnPayHistoryClickCallbacks;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/6/13  17:52
 * Project: Muzi
 * 缴费记录 列表适配器
 */
public class PaymentHistoryAdapter extends RecyclerView.Adapter {

    private ArrayList<PayRecordBean> payHistoryBeen;
    private OnPayHistoryClickCallbacks onPayHistoryClickCallbacks;

    public PaymentHistoryAdapter() {
        payHistoryBeen = new ArrayList<>();
    }

    public void setOnPayHistoryClickCallbacks(OnPayHistoryClickCallbacks onPayHistoryClickCallbacks) {
        this.onPayHistoryClickCallbacks = onPayHistoryClickCallbacks;
    }

    public ArrayList<PayRecordBean> getPayHistoryBeen() {
        return payHistoryBeen;
    }

    /**
     * 更新数据
     *
     * @param payHistoryBeen 数据列表
     */
    public boolean changeDataSet(ArrayList<PayRecordBean> payHistoryBeen) {
        if (payHistoryBeen != null && payHistoryBeen.size() > 0) {
            this.payHistoryBeen.clear();
            this.payHistoryBeen.addAll(payHistoryBeen);
            notifyDataSetChanged();
            return true;
        } else {
            this.payHistoryBeen.clear();
            notifyDataSetChanged();
            return false;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PayHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payhistory, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PayHistoryViewHolder viewHolder = (PayHistoryViewHolder) holder;
        PayRecordBean historyBean = payHistoryBeen.get(position);
        viewHolder.date.setText(historyBean.getChargeTime().replaceFirst(" ", "\n"));
        viewHolder.money.setText(historyBean.getChargeSum() + "元");
        viewHolder.type.setText(historyBean.getName());
    }

    @Override
    public int getItemCount() {
        return payHistoryBeen.size();
    }

    class PayHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView date, money, type;

        public PayHistoryViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.textView_payhistory_time);
            money = (TextView) itemView.findViewById(R.id.textView_payhistory_money);
            type = (TextView) itemView.findViewById(R.id.textView_payhistory_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onPayHistoryClickCallbacks != null) {
                onPayHistoryClickCallbacks.onPayHistoryClick(payHistoryBeen.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}
