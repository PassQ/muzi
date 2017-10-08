package com.ocwvar.muzi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/10  17:57
 * Project: Muzi
 * 报修状态Bean
 */
public class RepireStatusBean implements Parcelable {

    public static final Creator<RepireStatusBean> CREATOR = new Creator<RepireStatusBean>() {
        @Override
        public RepireStatusBean createFromParcel(Parcel in) {
            return new RepireStatusBean(in);
        }

        @Override
        public RepireStatusBean[] newArray(int size) {
            return new RepireStatusBean[size];
        }
    };
    String ID;
    String finishServiceTime;
    String location;
    String servicemanTel;
    String servicemanName;
    String servicemanID;
    String serviceType;
    String submitTime;
    String currentState;
    String title;
    String context;
    private String[] pictures;
    private String[] picturesThu;
    private String[] picturesID;

    public RepireStatusBean() {
    }

    protected RepireStatusBean(Parcel in) {
        ID = in.readString();
        finishServiceTime = in.readString();
        location = in.readString();
        servicemanTel = in.readString();
        servicemanName = in.readString();
        servicemanID = in.readString();
        serviceType = in.readString();
        submitTime = in.readString();
        currentState = in.readString();
        title = in.readString();
        context = in.readString();
        picturesID = in.createStringArray();
        pictures = in.createStringArray();
        picturesThu = in.createStringArray();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFinishServiceTime() {
        return finishServiceTime;
    }

    public void setFinishServiceTime(String finishServiceTime) {
        this.finishServiceTime = finishServiceTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServicemanTel() {
        return servicemanTel;
    }

    public void setServicemanTel(String servicemanTel) {
        this.servicemanTel = servicemanTel;
    }

    public String getServicemanName() {
        return servicemanName;
    }

    public void setServicemanName(String servicemanName) {
        this.servicemanName = servicemanName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getServicemanID() {
        return servicemanID;
    }

    public void setServicemanID(String servicemanID) {
        this.servicemanID = servicemanID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public String[] getPicturesThu() {
        return picturesThu;
    }

    public void setPicturesThu(String[] picturesThu) {
        this.picturesThu = picturesThu;
    }

    public String[] getPicturesID() {
        return picturesID;
    }

    public void setPicturesID(String[] picturesID) {
        this.picturesID = picturesID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(finishServiceTime);
        dest.writeString(location);
        dest.writeString(servicemanTel);
        dest.writeString(servicemanName);
        dest.writeString(servicemanID);
        dest.writeString(serviceType);
        dest.writeString(submitTime);
        dest.writeString(currentState);
        dest.writeString(title);
        dest.writeString(context);
        dest.writeStringArray(picturesID);
        dest.writeStringArray(pictures);
        dest.writeStringArray(picturesThu);
    }

}
