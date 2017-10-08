package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/26  17:03
 * Project: Muzi
 */
public interface OnUploadPaymentCallback {

    /**
     * 缴费成功
     *
     * @param orderNo 缴费成功的订单号
     */
    void onUploadCompleted(String roomID, String chargeTypeID, String payCount, String orderNo);

    /**
     * 缴费失败
     *
     * @param isException  是否为异常
     * @param roomID       房间号
     * @param chargeTypeID 支付类型
     * @param payCount     订单金额
     * @param orderNo      订单号
     */
    void onUploadFailed(boolean isException, String roomID, String chargeTypeID, String payCount, String orderNo);

}
