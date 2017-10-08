package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.Beans.ReplyTabBean;

import java.util.ArrayList;

/**
 * Created by 覃毅 on 2016/12/26.
 * 获取商家预设评论列表回调
 */

public interface OnGetReplyListCallback {


    /**
     * 获取成功
     *
     * @param list               列表对象集合
     * @param total              所获取图片总数
     *
     */
    void onGetReplyListCompleted(ArrayList<BulletinBean> list, int total);

    /**
     * 获取失败
     *
     * @param isException  是否为异常
     *
     *

     */
    void onGetReplyListFailed(boolean isException);


}