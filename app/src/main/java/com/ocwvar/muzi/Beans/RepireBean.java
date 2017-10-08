package com.ocwvar.muzi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Data: 2016/7/4 18:37
 * Project: Muzi
 * 维修信息
 */
public class RepireBean implements Parcelable {

    public static final Creator<RepireBean> CREATOR = new Creator<RepireBean>() {
        @Override
        public RepireBean createFromParcel(Parcel in) {
            return new RepireBean(in);
        }

        @Override
        public RepireBean[] newArray(int size) {
            return new RepireBean[size];
        }
    };
    private String reserveTime;
    private String proprietorID;
    private String remark;
    private String principalAcceptTime;
    private String evaluateReson;
    private String ID;
    private String servicemanID;
    private String AttrIDs_O;
    private String acceptDirectorID;
    private String serviceTypeID;
    private String overdueTime;
    private String location;
    private String currentState;        //1是预指定 2是公开 3是已受理 4待评价 5是维修结束 6是强制指派
    private String evaluateTime;
    private String evaluateResault;
    private String servicemanAcceptTime;
    private String finishServiceTime;
    private String communityID;
    private String submitTime;
    private String proprietorTel;
    private String compulsionTime;
    private String title;
    private String Content;
    private String preview;
    private String servicemanTel;
    private String finalServicemanID;
    private String directorAcceptTime;
    private String submitCounts;
    private String groupID;
    private String preServicemanID;
    private String AttrIDs_P;
    private String[] pictures;
    private String[] picturesID;
    private String[] picturesThu;
    private int totalCount;

    public RepireBean() {
    }

    protected RepireBean(Parcel in) {
        reserveTime = in.readString();
        proprietorID = in.readString();
        remark = in.readString();
        principalAcceptTime = in.readString();
        evaluateReson = in.readString();
        ID = in.readString();
        servicemanID = in.readString();
        AttrIDs_O = in.readString();
        acceptDirectorID = in.readString();
        serviceTypeID = in.readString();
        overdueTime = in.readString();
        location = in.readString();
        currentState = in.readString();
        evaluateTime = in.readString();
        evaluateResault = in.readString();
        servicemanAcceptTime = in.readString();
        finishServiceTime = in.readString();
        communityID = in.readString();
        submitTime = in.readString();
        proprietorTel = in.readString();
        compulsionTime = in.readString();
        title = in.readString();
        Content = in.readString();
        preview = in.readString();
        servicemanTel = in.readString();
        finalServicemanID = in.readString();
        directorAcceptTime = in.readString();
        submitCounts = in.readString();
        groupID = in.readString();
        preServicemanID = in.readString();
        AttrIDs_P = in.readString();
        pictures = in.createStringArray();
        picturesThu = in.createStringArray();
        picturesID = in.createStringArray();
        totalCount = in.readInt();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getProprietorID() {
        return proprietorID;
    }

    public void setProprietorID(String proprietorID) {
        this.proprietorID = proprietorID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrincipalAcceptTime() {
        return principalAcceptTime;
    }

    public void setPrincipalAcceptTime(String principalAcceptTime) {
        this.principalAcceptTime = principalAcceptTime;
    }

    public String getEvaluateReson() {
        return evaluateReson;
    }

    public void setEvaluateReson(String evaluateReson) {
        this.evaluateReson = evaluateReson;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getServicemanID() {
        return servicemanID;
    }

    public void setServicemanID(String servicemanID) {
        this.servicemanID = servicemanID;
    }

    public String getAttrIDs_O() {
        return AttrIDs_O;
    }

    public void setAttrIDs_O(String attrIDs_O) {
        AttrIDs_O = attrIDs_O;
    }

    public String getAcceptDirectorID() {
        return acceptDirectorID;
    }

    public void setAcceptDirectorID(String acceptDirectorID) {
        this.acceptDirectorID = acceptDirectorID;
    }

    public String getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(String overdueTime) {
        this.overdueTime = overdueTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getEvaluateResault() {
        return evaluateResault;
    }

    public void setEvaluateResault(String evaluateResault) {
        this.evaluateResault = evaluateResault;
    }

    public String getServicemanAcceptTime() {
        return servicemanAcceptTime;
    }

    public void setServicemanAcceptTime(String servicemanAcceptTime) {
        this.servicemanAcceptTime = servicemanAcceptTime;
    }

    public String getFinishServiceTime() {
        return finishServiceTime;
    }

    public void setFinishServiceTime(String finishServiceTime) {
        this.finishServiceTime = finishServiceTime;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getProprietorTel() {
        return proprietorTel;
    }

    public void setProprietorTel(String proprietorTel) {
        this.proprietorTel = proprietorTel;
    }

    public String getCompulsionTime() {
        return compulsionTime;
    }

    public void setCompulsionTime(String compulsionTime) {
        this.compulsionTime = compulsionTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getServicemanTel() {
        return servicemanTel;
    }

    public void setServicemanTel(String servicemanTel) {
        this.servicemanTel = servicemanTel;
    }

    public String getFinalServicemanID() {
        return finalServicemanID;
    }

    public void setFinalServicemanID(String finalServicemanID) {
        this.finalServicemanID = finalServicemanID;
    }

    public String getDirectorAcceptTime() {
        return directorAcceptTime;
    }

    public void setDirectorAcceptTime(String directorAcceptTime) {
        this.directorAcceptTime = directorAcceptTime;
    }

    public String getSubmitCounts() {
        return submitCounts;
    }

    public void setSubmitCounts(String submitCounts) {
        this.submitCounts = submitCounts;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getPreServicemanID() {
        return preServicemanID;
    }

    public void setPreServicemanID(String preServicemanID) {
        this.preServicemanID = preServicemanID;
    }

    public String getAttrIDs_P() {
        return AttrIDs_P;
    }

    public void setAttrIDs_P(String attrIDs_P) {
        AttrIDs_P = attrIDs_P;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reserveTime);
        parcel.writeString(proprietorID);
        parcel.writeString(remark);
        parcel.writeString(principalAcceptTime);
        parcel.writeString(evaluateReson);
        parcel.writeString(ID);
        parcel.writeString(servicemanID);
        parcel.writeString(AttrIDs_O);
        parcel.writeString(acceptDirectorID);
        parcel.writeString(serviceTypeID);
        parcel.writeString(overdueTime);
        parcel.writeString(location);
        parcel.writeString(currentState);
        parcel.writeString(evaluateTime);
        parcel.writeString(evaluateResault);
        parcel.writeString(servicemanAcceptTime);
        parcel.writeString(finishServiceTime);
        parcel.writeString(communityID);
        parcel.writeString(submitTime);
        parcel.writeString(proprietorTel);
        parcel.writeString(compulsionTime);
        parcel.writeString(title);
        parcel.writeString(Content);
        parcel.writeString(preview);
        parcel.writeString(servicemanTel);
        parcel.writeString(finalServicemanID);
        parcel.writeString(directorAcceptTime);
        parcel.writeString(submitCounts);
        parcel.writeString(groupID);
        parcel.writeString(preServicemanID);
        parcel.writeString(AttrIDs_P);
        parcel.writeStringArray(pictures);
        parcel.writeStringArray(picturesThu);
        parcel.writeStringArray(picturesID);
        parcel.writeInt(totalCount);
    }
}
