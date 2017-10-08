package com.ocwvar.muzi.Beans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/4  10:24
 * Project: Muzi
 * 主界面信息列表Bean
 */

import android.graphics.Color;

public class InfoListBean {

    int typeColor = Color.rgb(99, 184, 255);
    ArtclesBean bean;
    String url;
    String type;
    String title;

    public InfoListBean(int typeColor, String type, ArtclesBean bean) {
        this.typeColor = typeColor;
        this.type = type;
        this.bean = bean;
        this.title = bean.getTitle();
    }

    public InfoListBean(int typeColor, String type, String url, String title) {
        this.typeColor = typeColor;
        this.type = type;
        this.url = url;
        this.title = title;
    }

    public InfoListBean() {
    }

    public int getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(int typeColor) {
        this.typeColor = typeColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArtclesBean getBean() {
        return bean;
    }

    public void setBean(ArtclesBean bean) {
        this.bean = bean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoListBean that = (InfoListBean) o;

        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
