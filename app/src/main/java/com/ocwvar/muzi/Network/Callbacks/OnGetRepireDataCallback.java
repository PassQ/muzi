package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.RepireBean;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Data: 2016/7/5 16:52
 * Project: Muzi
 * 获取维修数据列表
 */
public interface OnGetRepireDataCallback {

    /**
     * 获取已受理数据
     *
     * @param repiredList 数据列表
     * @param totalCount  一共的数量
     */
    void onGotRepiredData(ArrayList<RepireBean> repiredList, int totalCount);

    /**
     * 获取未受理数据
     *
     * @param unRepiredList 数据列表
     * @param totalCount    一共的数量
     */
    void onGotUnRepiredData(ArrayList<RepireBean> unRepiredList, int totalCount);

    /**
     * 获取数据失败
     */
    void onFailed();

}
