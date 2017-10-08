package com.ocwvar.muzi.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.PaymentBean;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.PayBox;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Data: 2016/7/13 22:07
 * Project: Muzi
 * 多选支付界面
 */
public class MuiltPaymentAdapter extends RecyclerView.Adapter {

    private ArrayList<PaymentBean> paymentList;
    private PayBox box;

    public MuiltPaymentAdapter(PayBox box) {
        paymentList = new ArrayList<>();
        this.box = box;
    }

    private void addTestItems() {
        paymentList.add(new PaymentBean("100", "项目1", "52.4", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目2", "324", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目3", "122.4", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目4", "151.4", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目5", "211", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目6", "0.5", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目7", "0.1", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目8", "524", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目9", "5.4", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目10", "52.7", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目11", "12", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目12", "3", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目13", "98", "1", "1"));
        paymentList.add(new PaymentBean("100", "项目14", "2.1", "1", "1"));
    }


    public void setPayBox(PayBox box) {
        this.box = box;
    }

    public void setPaymentData(ArrayList<PaymentBean> paymentList) {
        if (paymentList != null) {
            this.paymentList.clear();
            this.paymentList.addAll(paymentList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaymentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_muilt, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PaymentBean paymentBean = paymentList.get(position);
        PaymentViewHolder paymentViewHolder = (PaymentViewHolder) holder;

        //设置支付项目名称
        paymentViewHolder.title.setText(paymentBean.getName());
        //设置单价显示
        paymentViewHolder.singel.setText("单价:" + paymentBean.getSinglePrice() + "元");
        //设置当前余额数据
        if (paymentBean.getSumsBalance().contains("-")) {
            //如果当前余额字符内存在负号 , 则文字标红
            paymentViewHolder.total.setTextColor(Color.RED);
        } else {
            paymentViewHolder.total.setTextColor(Color.DKGRAY);
        }
        paymentViewHolder.total.setText("余额 " + paymentBean.getSumsBalance() + "元");

        //如果该项目存在于盒子中 , 则显示对应的数据
        if (box.isInList(paymentBean)) {

            //先获取保存的支付倍数和单价
            int count = box.getPaymentCount(paymentBean);
            float price = Float.parseFloat(paymentBean.getSinglePrice()) * count;

            //使操作版面可见
            paymentViewHolder.panel.setVisibility(View.VISIBLE);
            //使该项为选中状态
            paymentViewHolder.title.setOnCheckedChangeListener(null);
            paymentViewHolder.title.setChecked(true);
            paymentViewHolder.title.setOnCheckedChangeListener(paymentViewHolder);
            //设置支付倍数指示器
            paymentViewHolder.addCount.setText(String.format("%d", count));
            //根据支付倍数显示支付总金额
            paymentViewHolder.payCount.setText(String.format("%.2f", price) + "元");

        } else {
            //如果该项目不存在于盒子中 , 则显示默认数据

            paymentViewHolder.title.setOnCheckedChangeListener(null);
            paymentViewHolder.title.setChecked(false);
            paymentViewHolder.title.setOnCheckedChangeListener(paymentViewHolder);
            paymentViewHolder.panel.setVisibility(View.GONE);
            paymentViewHolder.addCount.setText("1");
            paymentViewHolder.payCount.setText("0元");
        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        View panel;
        CheckBox title;
        TextView addCount, payCount, singel, total;
        ImageView add, cut;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            panel = itemView.findViewById(R.id.view_payment_panel);
            title = (CheckBox) itemView.findViewById(R.id.radioButton_payment_title);
            addCount = (TextView) itemView.findViewById(R.id.textView_payment_addCount);
            payCount = (TextView) itemView.findViewById(R.id.textView_payment_payCount);
            total = (TextView) itemView.findViewById(R.id.textView_payment_total);
            singel = (TextView) itemView.findViewById(R.id.textView_payment_singel);
            add = (ImageView) itemView.findViewById(R.id.imageView_payment_add);
            cut = (ImageView) itemView.findViewById(R.id.imageView_payment_cut);
            title.setOnCheckedChangeListener(this);
            add.setOnClickListener(this);
            cut.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //从View的字符串获取当前的支付倍数
            int count = Integer.parseInt(addCount.getText().toString());
            //从盒子中获取支付单价
            float price = Float.parseFloat(paymentList.get(getAdapterPosition()).getSinglePrice());

            switch (v.getId()) {
                case R.id.imageView_payment_add:
                    //点击增加倍数 1
                    count += 1;
                    //倍数指示器
                    addCount.setText(String.format("%d", count));
                    //更新支付盒子内的数据
                    box.setPayCount(paymentList.get(getAdapterPosition()), count);
                    //更新总支付金额
                    price = price * count;    //总金额
                    payCount.setText(String.format("%.2f", price) + "元");
                    break;
                case R.id.imageView_payment_cut:
                    //最小支付倍数为 1
                    if (count > 1) {
                        //点击减少倍数 1
                        count -= 1;
                        //倍数指示器
                        addCount.setText(String.format("%d", count));
                        //更新支付盒子内的数据
                        box.setPayCount(paymentList.get(getAdapterPosition()), count);
                        //更新总支付金额
                        price = price * count;    //总金额
                        payCount.setText(String.format("%.2f", price) + "元");
                    }
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked && box.isInList(paymentList.get(getAdapterPosition()))) {
                //发生移除动作  同时数据存在于盒子内 则移除 , 同时设置版面不可见
                box.removePaymentItem(paymentList.get(getAdapterPosition()));
                //清零总支付金额
                payCount.setText("0元");
                //重置购买倍数为 1
                addCount.setText("1");
                //使操作版面不可见
                panel.setVisibility(View.GONE);
            } else if (isChecked) {
                //发生添加操作  将数据保存于盒子内 同时设置版面可见
                box.addPaymentItem(paymentList.get(getAdapterPosition()), 1);
                //使操作版面可见
                panel.setVisibility(View.VISIBLE);
                //设置总支付金额默认为 1 倍时的价格
                payCount.setText(paymentList.get(getAdapterPosition()).getSinglePrice() + "元");
            }
        }

    }

}
