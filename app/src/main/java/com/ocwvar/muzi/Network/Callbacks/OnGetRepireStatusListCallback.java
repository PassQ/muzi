package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.RepireStatusBean;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/10  18:03
 * Project: Muzi
 * 获取报修状态列表
 */
public interface OnGetRepireStatusListCallback {

    void onGotRepireStatusListCompleted(ArrayList<RepireStatusBean> repireStatusBeen);

    void onGotRepireStatusListFailed(boolean isException);

}
