package com.ocwvar.muzi.Utils;

import com.ocwvar.muzi.Beans.PaymentBean;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Utils
 * Data: 2016/7/13 22:00
 * Project: Muzi
 * 支付项目存放
 */
public class PayBox {

    private ArrayList<PaymentBean> payItems;
    private ArrayList<Integer> payCounts;

    public PayBox() {
        payItems = new ArrayList<>();
        payCounts = new ArrayList<>();
    }

    /**
     * 添加支付数据
     *
     * @param paymentBean 支付信息
     * @param payCount    支付倍数
     */
    public void addPaymentItem(PaymentBean paymentBean, int payCount) {
        payItems.add(paymentBean);
        payCounts.add(payCount);
    }

    /**
     * 移除支付数据
     *
     * @param paymentBean 支付信息
     */
    public void removePaymentItem(PaymentBean paymentBean) {
        int position = payItems.indexOf(paymentBean);
        payItems.remove(position);
        payCounts.remove(position);
    }

    /**
     * 获取支付项目的支付倍数
     *
     * @param paymentBean 支付信息
     * @return 倍数
     */
    public int getPaymentCount(PaymentBean paymentBean) {
        int position = payItems.indexOf(paymentBean);
        return payCounts.get(position);
    }

    /**
     * 设置支付倍数
     *
     * @param paymentBean 支付信息
     * @param payCount    倍数
     */
    public void setPayCount(PaymentBean paymentBean, int payCount) {
        int position = payItems.indexOf(paymentBean);
        payCounts.set(position, payCount);
    }

    /**
     * 检测支付数据是否在列表内
     *
     * @param paymentBean 支付信息
     * @return 是否在列表内存在
     */
    public boolean isInList(PaymentBean paymentBean) {
        return payItems.contains(paymentBean);
    }

    public ArrayList<Integer> getPayCounts() {
        return payCounts;
    }

    public ArrayList<PaymentBean> getPayItems() {
        return payItems;
    }
}
