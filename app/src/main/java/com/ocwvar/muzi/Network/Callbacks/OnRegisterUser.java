package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/12  13:53
 * Project: Muzi
 * 注册新用户回调
 */
public interface OnRegisterUser {

    void onCompleted();

    void onFailed(boolean isException);

}
