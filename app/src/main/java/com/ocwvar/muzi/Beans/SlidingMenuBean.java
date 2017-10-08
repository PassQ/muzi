package com.ocwvar.muzi.Beans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/5  22:16
 * Project: Muzi
 * 侧滑菜单Bean
 */

public class SlidingMenuBean {

    int iconRes;
    String title;

    public SlidingMenuBean(int iconRes, String title) {
        this.iconRes = iconRes;
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
