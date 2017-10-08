package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/12  14:15
 * Project: Muzi
 * 检查用户是否存在接口
 */
public interface OnCheckUser {

    void onCompleted(String checkID, boolean isExist);

    void onFailed();

}
