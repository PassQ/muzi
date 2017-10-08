package com.ocwvar.muzi.Beans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/6  9:16
 * Project: Muzi
 * 用户信息Bean
 */

public class UserBean {

    private String userHeadImage;

    private String userID;
    private String registerTime;

    private String loginID;
    private String password;

    private String phoneNumber;
    private String userName;
    private String communityID;
    private String communityName;
    private String apartment;
    private String unit;
    private String unitName;
    private String room;
    private String roomName;

    private UserType userType;

    public UserBean() {
        //可以在这里设置默认值
        userType = UserType.GUEST;
    }

    public UserBean(UserBean copySource) {
        userID = copySource.getUserID();
        registerTime = copySource.getRegisterTime();

        loginID = copySource.getLoginID();
        password = copySource.getPassword();

        phoneNumber = copySource.getPhoneNumber();
        userName = copySource.getUserName();
        communityID = copySource.getCommunityID();
        communityName = copySource.getCommunityName();
        apartment = copySource.getApartment();
        unit = copySource.getUnit();
        unitName = copySource.getUnitName();
        room = copySource.getRoom();
        roomName = copySource.getRoomName();

        userType = copySource.getUserType();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
