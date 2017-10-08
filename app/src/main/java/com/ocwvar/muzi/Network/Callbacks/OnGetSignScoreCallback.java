package com.ocwvar.muzi.Network.Callbacks;

import java.util.List;




import java.util.List;

/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/17  11:48
 * Project: Muzi
 */
public interface OnGetSignScoreCallback {

    /**
     * 获取成功
     *
     * @param userTel 用户手机号
     * @param score 用户积分总数
     *
     */
    void onGetSignScoreCompleted(String userTel, String score);

    /**
     * 获取失败
     *
     * @param isException  是否为异常
     * @param userTel      用户手机号
     *

     */
    void onGetSignScoreFailed(boolean isException, String userTel);

}