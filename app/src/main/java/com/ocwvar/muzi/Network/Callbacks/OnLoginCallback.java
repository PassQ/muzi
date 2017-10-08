package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.UserBean;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/5  13:17
 * Project: Muzi
 * 登录状态接口
 */

public interface OnLoginCallback {

    void onLoginCompleted(String username, UserBean userBean);

    void onLoginFailed(String message, boolean isException);

}
