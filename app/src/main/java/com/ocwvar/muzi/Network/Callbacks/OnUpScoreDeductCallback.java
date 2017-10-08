package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/29  15:03
 * Project: Muzi
 */
public interface OnUpScoreDeductCallback {

    /**
     * 取消预约成功
     *
     *
     *
     */
    void onUpScoreDeductCompleted( boolean isSuccessed);

    /**
     * 取消预约失败
     *
     * @param isException  是否为异常

     */
    void onUpScoreDeductFailed(boolean isException);

}