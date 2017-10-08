package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/10  21:26
 * Project: Muzi
 * 上传报修评分回调
 */
public interface OnUploadRepireScoreCallback {

    void onUploadScoreCompleted();

    void onUploadScoreFailed(boolean isException);

}
