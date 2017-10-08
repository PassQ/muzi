package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.UserBean;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/11  10:55
 * Project: Muzi
 * 获取用户信息回调
 */
public interface OnGetUserInfoCallback {

    void onCompleted(UserBean userBean);

    void onFailed(boolean isException);

}
