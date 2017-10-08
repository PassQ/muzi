package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.PayRecordBean;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/6/13  18:22
 * Project: Muzi
 * 缴费历史 点击事件回调
 */
public interface OnPayHistoryClickCallbacks {

    /**
     * 缴费历史 点击事件回调
     *
     * @param bean     缴费信息Bean
     * @param position 点击位置
     */
    void onPayHistoryClick(PayRecordBean bean, int position);

}
