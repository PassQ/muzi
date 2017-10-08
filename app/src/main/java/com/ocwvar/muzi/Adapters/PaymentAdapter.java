package com.ocwvar.muzi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.PaymentBean;
import com.ocwvar.muzi.Network.Callbacks.OnPaymentChangedCallback;
import com.ocwvar.muzi.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/14  11:40
 * Project: Muzi
 * 缴费操作Adapter
 */
public class PaymentAdapter extends RecyclerView.Adapter {

    RadioButton lastRadioButton;
    View lastPanel;
    int addCountNumber = 0;
    float payCountNumber = 0f;
    int selectedPosition = -1;
    private ArrayList<PaymentBean> paymentList;
    private OnPaymentChangedCallback onPaymentChangedCallback;

    public PaymentAdapter(OnPaymentChangedCallback onPaymentChangedCallback) {
        this.onPaymentChangedCallback = onPaymentChangedCallback;
        paymentList = new ArrayList<>();

    }

    /**
     * 添加数据
     *
     * @param paymentList 数据源
     */
    public void setPaymentList(ArrayList<PaymentBean> paymentList) {
        this.paymentList.clear();
        this.paymentList.addAll(paymentList);
        this.paymentList.add(new PaymentBean());    //添加的这个是空白项 , 用于底部留空
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position != paymentList.size() - 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new EmptyViewHolder(new View(parent.getContext()));
            default:
            case 1:
                return new PaymentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < paymentList.size() - 1) {
            PaymentViewHolder viewHolder = (PaymentViewHolder) holder;
            PaymentBean paymentBean = paymentList.get(position);

            viewHolder.title.setText(paymentBean.getName());
            viewHolder.singel.setText("单价: " + Float.toString(handleNumber(Double.parseDouble(paymentBean.getSinglePrice()))));

            if (position == selectedPosition) {
                viewHolder.panel.setVisibility(View.VISIBLE);
                viewHolder.title.setChecked(true);
                viewHolder.addCount.setText(Integer.toString(addCountNumber));
                viewHolder.payCount.setText(Float.toString(payCountNumber));
                lastPanel = viewHolder.panel;
                lastRadioButton = viewHolder.title;
            } else {
                viewHolder.panel.setVisibility(View.GONE);
                viewHolder.title.setChecked(false);
                viewHolder.addCount.setText("0");
                viewHolder.payCount.setText("0");
            }

        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    /**
     * 四舍五入求金额  (保留两位小数)
     *
     * @param number 要处理的数
     * @return 处理完成后的数
     */
    public float handleNumber(double number) {
        final int scaleCount = 2; //保留两位小数
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal.setScale(scaleCount, RoundingMode.HALF_UP);  //裁剪小数模式是 四舍五入
        number = 0d;
        return bigDecimal.floatValue();
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        View panel;
        RadioButton title;
        TextView addCount, payCount, singel;
        ImageView add, cut;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            panel = itemView.findViewById(R.id.view_payment_panel);
            title = (RadioButton) itemView.findViewById(R.id.radioButton_payment_title);
            addCount = (TextView) itemView.findViewById(R.id.textView_payment_addCount);
            payCount = (TextView) itemView.findViewById(R.id.textView_payment_payCount);
            singel = (TextView) itemView.findViewById(R.id.textView_payment_singel);
            add = (ImageView) itemView.findViewById(R.id.imageView_payment_add);
            cut = (ImageView) itemView.findViewById(R.id.imageView_payment_cut);
            title.setOnCheckedChangeListener(this);
            add.setOnClickListener(this);
            cut.setOnClickListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //当此项被选中 同时 上一次选择的不是此项
            if (isChecked && (selectedPosition != getAdapterPosition())) {

                //如果上一个被选择的项目还在当前视图里 , 则重置状态
                if (lastPanel != null) lastPanel.setVisibility(View.GONE);
                if (lastRadioButton != null) lastRadioButton.setChecked(false);

                //设置当前项控制板可见
                panel.setVisibility(View.VISIBLE);

                //设置当前项为最新项
                lastPanel = panel;
                lastRadioButton = title;

                //重置数据
                addCountNumber = 0;
                payCountNumber = 0;
                selectedPosition = getAdapterPosition();

                //更新接口状态
                onPaymentChangedCallback.onPaymentChanged(paymentList.get(getAdapterPosition()), getAdapterPosition(), payCountNumber);
            } else if (!isChecked) {
                addCount.setText("0");
                payCount.setText("0");
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageView_payment_add:
                    addCountNumber += 1;
                    payCountNumber = handleNumber(Double.parseDouble(paymentList.get(getAdapterPosition()).getSinglePrice()) * addCountNumber);
                    addCount.setText(Integer.toString(addCountNumber));
                    payCount.setText(Float.toString(payCountNumber));
                    onPaymentChangedCallback.onPaymentChanged(paymentList.get(getAdapterPosition()), getAdapterPosition(), payCountNumber);
                    break;
                case R.id.imageView_payment_cut:
                    if (addCountNumber - 1 >= 0) {
                        addCountNumber -= 1;
                        payCountNumber = handleNumber(Double.parseDouble(paymentList.get(getAdapterPosition()).getSinglePrice()) * addCountNumber);
                        addCount.setText(Integer.toString(addCountNumber));
                        payCount.setText(Float.toString(payCountNumber));
                        onPaymentChangedCallback.onPaymentChanged(paymentList.get(getAdapterPosition()), getAdapterPosition(), payCountNumber);
                    }
                    break;
            }
        }

        /**
         * 四舍五入求金额  (保留两位小数)
         *
         * @param number 要处理的数
         * @return 处理完成后的数
         */
        public float handleNumber(double number) {
            final int scaleCount = 2; //保留两位小数
            BigDecimal bigDecimal = new BigDecimal(number);
            bigDecimal.setScale(scaleCount, RoundingMode.HALF_UP);  //裁剪小数模式是 四舍五入
            number = 0d;
            return bigDecimal.floatValue();
        }

    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 1000);
            itemView.setLayoutParams(params);
        }
    }

}
