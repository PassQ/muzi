package com.ocwvar.muzi.Beans;

/**
 * Created by 覃毅 on 2016/12/29.
 * 提交预约/评论信息所返回的对象
 */

public class OrderBean {
    int commentID;
    boolean isSuccessed;
    String message;

    public boolean isSuccessed() {
        return isSuccessed;
    }

    public void setSuccessed(boolean successed) {
        isSuccessed = successed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }
}
