package com.ocwvar.muzi.Network.Callbacks;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/10  12:03
 * Project: Muzi
 */
public interface OnUploadRequestRepireCallback {

    void onUploadCompleted();

    void onUploadFailed(boolean isException);

}
