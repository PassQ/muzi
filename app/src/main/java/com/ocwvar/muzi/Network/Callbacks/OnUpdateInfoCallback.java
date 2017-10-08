package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.UserBean;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/6  11:56
 * Project: Muzi
 * 更新用户信息接口
 */

public interface OnUpdateInfoCallback {

    void completed(UserBean userBean);

    void failed(boolean syncFailed);

}
