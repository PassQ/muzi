package com.ocwvar.muzi.Network.Callbacks;

import android.graphics.Bitmap;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/15  0:29
 * Project: Muzi
 * 用户头像上传回调
 */
public interface OnUploadUserHeadCallback {

    void onUploaded(Bitmap bitmap, String imageURL);

    void onUploadFailed(boolean isException);

}
