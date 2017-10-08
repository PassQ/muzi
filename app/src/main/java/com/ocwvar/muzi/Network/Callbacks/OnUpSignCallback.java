package com.ocwvar.muzi.Network.Callbacks;


/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/15  15:03
 * Project: Muzi
 */
public interface OnUpSignCallback {

    /**
     * 签到成功
     *
     * @param userTel 用户手机号
     * @param date 签到当日时间
     */
    void onUpSignCompleted(String userTel, String date, String massage, String score);

    /**
     * 签到失败
     *
     * @param isException  是否为异常
     * @param userTel      用户手机号
     * @param date         签到时间

     */
    void onUpSignFailed(boolean isException, String userTel, String date);

}
