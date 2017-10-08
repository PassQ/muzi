package com.ocwvar.muzi.Network.Callbacks;



/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/29  15:03
 * Project: Muzi
 */
public interface OnUpOrderCallback {

    /**
     * 签到成功
     *
     *
     * @param massage           返回信息
     */
    void onUpOrderCompleted( String massage, boolean isSuccessed);

    /**
     * 签到失败
     *
     * @param isException  是否为异常

     */
    void onUpOrderFailed(boolean isException);

}
