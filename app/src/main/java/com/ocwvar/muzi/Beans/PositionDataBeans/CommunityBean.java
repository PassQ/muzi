package com.ocwvar.muzi.Beans.PositionDataBeans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.PositionDataBeans.Beans
 * Date: 2016/5/8  1:09
 * Project: Muzi
 * 小区数据Bean
 */
public class CommunityBean {

    String belongArea;
    String name;
    String address;
    String ID;
    String telephone;
    boolean isHQ;

    public CommunityBean() {
    }

    public CommunityBean(String belongArea, String name, String address, String ID, String telephone, boolean isHQ) {
        this.belongArea = belongArea;
        this.name = name;
        this.address = address;
        this.ID = ID;
        this.telephone = telephone;
        this.isHQ = isHQ;
    }

    public String getBelongArea() {
        return belongArea;
    }

    public void setBelongArea(String belongArea) {
        this.belongArea = belongArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isHQ() {
        return isHQ;
    }

    public void setHQ(boolean HQ) {
        isHQ = HQ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommunityBean that = (CommunityBean) o;

        if (isHQ != that.isHQ) return false;
        if (belongArea != null ? !belongArea.equals(that.belongArea) : that.belongArea != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (ID != null ? !ID.equals(that.ID) : that.ID != null) return false;
        return telephone != null ? telephone.equals(that.telephone) : that.telephone == null;

    }

    @Override
    public int hashCode() {
        int result = belongArea != null ? belongArea.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (ID != null ? ID.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (isHQ ? 1 : 0);
        return result;
    }

}
