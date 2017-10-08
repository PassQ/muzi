package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/26  17:18
 * Project: Muzi
 */
public interface OnCheckPaymentCallback {

    /**
     * 查询成功
     *
     * @param isExist 订单号是否存在
     */
    void onCheckedResult(boolean isExist);

    /**
     * 查询失败
     *
     * @param isException 是否为异常
     */
    void onCheckFailed(boolean isException);

}
