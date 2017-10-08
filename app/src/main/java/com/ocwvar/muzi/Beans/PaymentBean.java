package com.ocwvar.muzi.Beans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/13  22:49
 * Project: Muzi
 * 缴费信息Bean
 */
public class PaymentBean {

    String sumsBalance;
    String name;
    String singlePrice;
    String ID;
    String roomID;

    public PaymentBean() {
    }

    public PaymentBean(String sumsBalance, String name, String singlePrice, String ID, String roomID) {
        this.sumsBalance = sumsBalance;
        this.name = name;
        this.singlePrice = singlePrice;
        this.ID = ID;
        this.roomID = roomID;
    }

    public String getSumsBalance() {
        return sumsBalance;
    }

    public void setSumsBalance(String sumsBalance) {
        this.sumsBalance = sumsBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentBean that = (PaymentBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (singlePrice != null ? !singlePrice.equals(that.singlePrice) : that.singlePrice != null)
            return false;
        if (ID != null ? !ID.equals(that.ID) : that.ID != null) return false;
        return roomID != null ? roomID.equals(that.roomID) : that.roomID == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (singlePrice != null ? singlePrice.hashCode() : 0);
        result = 31 * result + (ID != null ? ID.hashCode() : 0);
        result = 31 * result + (roomID != null ? roomID.hashCode() : 0);
        return result;
    }

}
