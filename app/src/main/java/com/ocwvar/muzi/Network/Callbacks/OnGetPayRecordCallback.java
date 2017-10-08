package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.PayRecordBean;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/19  14:22
 * Project: Muzi
 * 获取缴费记录接口
 */
public interface OnGetPayRecordCallback {

    /**
     * 获取订单列表回调接口
     *
     * @param loadedCount   本次读取的数量
     * @param totalCount    数据库一共拥有的数据
     * @param roomID        房间号ID
     * @param payRecordBeen 获取到的数据列表.  当无数据时为NULL
     */
    void onGotPayRecords(int loadedCount, int totalCount, String roomID, ArrayList<PayRecordBean> payRecordBeen);

    /**
     * 订单获取失败回调接口
     *
     * @param isException 是否为异常
     */
    void onGotPayRecordsFailed(boolean isException);

}
