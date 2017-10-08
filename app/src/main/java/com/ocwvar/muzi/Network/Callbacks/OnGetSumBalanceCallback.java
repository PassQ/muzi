package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/19  17:33
 * Project: Muzi
 */
public interface OnGetSumBalanceCallback {

    void onGotSumBalance();

    void onGotSumBalanceFailed(boolean isException);

}
