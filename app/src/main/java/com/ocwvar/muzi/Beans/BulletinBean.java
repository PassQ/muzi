package com.ocwvar.muzi.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 覃毅 on 2016/12/21.
 * 商家模块列表对象
 */

public class BulletinBean implements Serializable {
    String update;//发布时间
    String offdays;
    int businessID;//商家ID
    int status;
    int orderStatus;//预约状态
    String address;//地址
    int orderID;//预约ID
    int commentID;//评论id
    String title;//题目
    String introduce;//介绍
    String score;//评分
    String imagePath;//头像图
    String phoneNum;//电话号码
    String detailedImagePath;//周边详细图
    String openTime;//开店时间
    String endTime;//关店时间
    int totalCount;//条目总数
    String massage;
   ArrayList<BusinessImageBean> imageList = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }


    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }



    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }



    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }



    public BulletinBean () {

    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public ArrayList<BusinessImageBean> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<BusinessImageBean> list) {
        for (BusinessImageBean bean: list) {
            if(!("").equals(bean.getImagePath())){
                imageList.add(bean);
            }
        }
    }

    public String getDetailedImagePath() {
        return detailedImagePath;
    }

    public void setDetailedImagePath(String detailedImagePath) {
        this.detailedImagePath = detailedImagePath;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getOffdays() {
        return offdays;
    }

    public void setOffdays(String offdays) {
        this.offdays = offdays;
    }
}
