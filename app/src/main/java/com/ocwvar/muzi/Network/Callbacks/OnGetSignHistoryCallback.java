package com.ocwvar.muzi.Network.Callbacks;


import java.util.List;

/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/16  9:44
 * Project: Muzi
 */
public interface OnGetSignHistoryCallback {

    /**
     * 获取成功
     *
     * @param userTel 用户手机号
     *
     */
    void onGetSignHistoryCompleted(String userTel, List<String> dateList);

    /**
     * 获取失败
     *
     * @param isException  是否为异常
     * @param userTel      用户手机号
     *

     */
    void onGetSignHistoryFailed(boolean isException, String userTel);

}
