package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/29  15:03
 * Project: Muzi
 * 获取积分抵消数据接口
 */
public interface OnGetScoreDeductCallback {

    /**
     * 获取积分抵消数据成功
     *
     *
     * @param useScore           兑换消耗的积分
     * @param restMoney          兑换后需要缴费的金额
     */
    void onGetScoreDeductCompleted( String useScore, String restMoney);

    /**
     * 获取积分抵消数据失败
     *
     * @param isException  是否为异常

     */
    void onGetScoreDeductFailed(boolean isException);

}