package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.BulletinBean;

import java.util.ArrayList;

/**
 * Created by 覃毅 on 2016/12/21.
 * 获取商家列表回调
 */

public interface OnGetCircumCallback {


    /**
     * 获取成功
     *
     * @param list          列表对象集合
     * @param totalCount    后台数据总量
     * @param pageIndex     加载页码
     * @param pageSize      加载的数量
     *
     *
     */
    void onGetCircumCompleted(int pageSize, int pageIndex, ArrayList<BulletinBean> list,int totalCount);

    /**
     * 获取失败失败
     *
     * @param isException  是否为异常
     *
     *

     */
    void onGetCircumFailed(boolean isException);


}
