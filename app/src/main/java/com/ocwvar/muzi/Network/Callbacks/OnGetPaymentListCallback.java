package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.PaymentBean;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/14  13:49
 * Project: Muzi
 * 获取缴费项目列表回调
 */
public interface OnGetPaymentListCallback {

    void onGotList(ArrayList<PaymentBean> paymentList);

    void onFailed(boolean isException);

}
