package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.PaymentBean;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/14  11:53
 * Project: Muzi
 * 缴费项目发生改变时的回调
 */
public interface OnPaymentChangedCallback {

    void onPaymentChanged(PaymentBean paymentBean, int position, float payCount);

}
