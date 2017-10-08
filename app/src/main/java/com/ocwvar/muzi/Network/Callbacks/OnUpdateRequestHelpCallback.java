package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/7  20:58
 * Project: Muzi
 * 提交业主诉求数据状态回调
 */
public interface OnUpdateRequestHelpCallback {

    void onCompleted();

    void onFailed(boolean isException);

}
