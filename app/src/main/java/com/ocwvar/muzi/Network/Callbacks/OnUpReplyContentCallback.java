package com.ocwvar.muzi.Network.Callbacks;


/**
 * Created by 覃毅
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/12/29  15:03
 * Project: Muzi
 */
public interface OnUpReplyContentCallback {

    /**
     * 预设评论上传成功
     *
     *
     * @param commentID           返回评论ID
     */
    void onUpReplyContentCompleted( int commentID, boolean isSuccessed);

    /**
     * 预设评论上传失败
     *
     * @param isException  是否为异常

     */
    void onUpReplyContentFailed(boolean isException);

}