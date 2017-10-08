package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Network.NetworkHelper;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/8  1:05
 * Project: Muzi
 * 区域获取数据列表接口
 */

public interface OnRequestDataListCallbacks {

    /**
     * 请求成功回调
     *
     * @param dataType  数据类型
     * @param datasList 数据列表
     */
    void onRequestCompleted(NetworkHelper.DataType dataType, ArrayList<Object> datasList);

    /**
     * 请求失败回调
     *
     * @param isException 是否为异常
     */
    void onFailed(boolean isException);

}
