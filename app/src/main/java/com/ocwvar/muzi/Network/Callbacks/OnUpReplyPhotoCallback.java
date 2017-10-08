package com.ocwvar.muzi.Network.Callbacks;


/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/29  15:03
 * Project: Muzi
 */
public interface OnUpReplyPhotoCallback {

    /**
     * 评论图片上传成功
     *
     *
     * @param massage           返回信息
     */
    void onUpReplyPhotoCompleted( String massage, boolean isSuccessed);

    /**
     * 评论图片上传失败
     *
     * @param isException  是否为异常

     */
    void onUpReplyPhotoFailed(boolean isException);

}