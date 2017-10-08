package com.ocwvar.muzi.Beans.PositionDataBeans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.PositionDataBeans.Beans
 * Date: 2016/5/8  12:12
 * Project: Muzi
 * 单元数据Bean
 */

public class UnitBean {

    String name;
    String id;
    String apartmentID;

    public UnitBean() {
    }

    public UnitBean(String name, String id, String apartmentID) {
        this.name = name;
        this.id = id;
        this.apartmentID = apartmentID;
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

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitBean unitBean = (UnitBean) o;

        if (name != null ? !name.equals(unitBean.name) : unitBean.name != null) return false;
        if (id != null ? !id.equals(unitBean.id) : unitBean.id != null) return false;
        return apartmentID != null ? apartmentID.equals(unitBean.apartmentID) : unitBean.apartmentID == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (apartmentID != null ? apartmentID.hashCode() : 0);
        return result;
    }
}
