package com.ocwvar.muzi.Network.Callbacks;

import android.support.annotation.Nullable;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.ArtcleType;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/4  12:08
 * Project: Muzi
 * 文章读取状态的接口
 */

public interface OnArtcleLoadCallback {

    /**
     * 获取文章完成时的回调
     *
     * @param loadedArtclesCount 当前读取的数量
     * @param totalArtclesCount  总共的数量
     * @param artcleType         文章的所属类型
     * @param communityID        所属的社区ID
     * @param arrayList          文章列表 当没有文章时，为NULL
     */
    void onArtcleLoadCompleted(int loadedArtclesCount, int totalArtclesCount, ArtcleType artcleType, int communityID, @Nullable ArrayList<ArtclesBean> arrayList);

    /**
     * 获取文章失败时的回调
     *
     * @param message 失败返回的信息
     * @param e       失败抛出的异常
     */
    void onArtcleLoadFailed(String message, Exception e);

}
