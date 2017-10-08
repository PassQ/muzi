package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;

import java.util.ArrayList;

/**
 * Created by 覃毅 on 2016/12/25.
 * 获取商家详情图片列表回调
 */

public interface OnGetBusinessImageCallback {


    /**
     * 获取成功
     *
     * @param imageList          列表对象集合
     * @param total              所获取图片总数
     *
     */
    void onGetBusinessImageCompleted(ArrayList<BusinessImageBean> imageList, int total);

    /**
     * 获取失败
     *
     * @param isException  是否为异常
     *
     *

     */
    void onGetBusinessImageFailed(boolean isException);


}