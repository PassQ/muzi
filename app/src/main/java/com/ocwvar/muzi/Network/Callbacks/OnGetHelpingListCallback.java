package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.ArtclesBean;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/6/13  14:39
 * Project: Muzi
 * 获取 我的爱心 列表回调
 */
public interface OnGetHelpingListCallback {

    /**
     * 获取 我的爱心 列表回调
     *
     * @param pageSize    获取页的单页数据
     * @param totalCount  后台一共的数据量
     * @param index       获取页的页码
     * @param artclesBeen 获取到的数据
     */
    void onGotHelpingList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen);

    /**
     * 获取 我的爱心 列表回调失败
     *
     * @param isException 是否为异常
     */
    void onGotHelpingFailed(boolean isException);

}
