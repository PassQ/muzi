package com.ocwvar.muzi.Beans.PositionDataBeans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.PositionDataBeans.Beans
 * Date: 2016/5/8  12:08
 * Project: Muzi
 * 楼号信息Bean
 */
public class ApartmentBean {

    String communityID;
    String name;
    String id;

    public ApartmentBean() {
    }

    public ApartmentBean(String communityID, String name, String id) {
        this.communityID = communityID;
        this.name = name;
        this.id = id;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApartmentBean that = (ApartmentBean) o;

        if (communityID != null ? !communityID.equals(that.communityID) : that.communityID != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = communityID != null ? communityID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
