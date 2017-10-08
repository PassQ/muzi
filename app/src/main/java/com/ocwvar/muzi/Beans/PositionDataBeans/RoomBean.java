package com.ocwvar.muzi.Beans.PositionDataBeans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.PositionDataBeans.Beans
 * Date: 2016/5/8  12:14
 * Project: Muzi
 */

public class RoomBean {

    String id;
    String name;
    String unitID;

    public RoomBean() {
    }

    public RoomBean(String id, String name, String unitID) {
        this.id = id;
        this.name = name;
        this.unitID = unitID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomBean roomBean = (RoomBean) o;

        if (id != null ? !id.equals(roomBean.id) : roomBean.id != null) return false;
        if (name != null ? !name.equals(roomBean.name) : roomBean.name != null) return false;
        return unitID != null ? unitID.equals(roomBean.unitID) : roomBean.unitID == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (unitID != null ? unitID.hashCode() : 0);
        return result;
    }
}
