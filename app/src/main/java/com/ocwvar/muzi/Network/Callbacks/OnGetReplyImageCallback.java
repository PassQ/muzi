package com.ocwvar.muzi.Network.Callbacks;


import com.ocwvar.muzi.Adapters.ReviewAdapter;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;

import java.util.ArrayList;

/**
 * Created by 覃毅 on 2016/12/25.
 * 获取评论列表图片列表回调
 */

public interface OnGetReplyImageCallback {


    /**
     * 获取成功
     *
     * @param imageList          列表对象集合
     *
     */
    void onGetReplyImageCompleted(ArrayList<BusinessImageBean> imageList, int count);

    /**
     * 获取失败
     *
     * @param isException  是否为异常
     *
     *

     */
    void onGetReplyImageFailed(boolean isException);


}