package com.ocwvar.muzi.Network.Callbacks;


/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/15  15:03
 * Project: Muzi
 */
public interface OnUpLikeCallback {

    /**
     * 点赞成功
     *
     * @param userTel 用户手机号
     * @param score  点赞所得积分
     * @param massage 点赞所返回的信息
     *
     */
    void onUpLikeCompleted(String userTel,String massage, String score);

    /**
     * 点赞失败
     *
     * @param isException  是否为异常
     * @param userTel      用户手机号
     *

     */
    void onUpLikeFailed(boolean isException, String userTel);

}
