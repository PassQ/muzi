package com.ocwvar.muzi.Beans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/4  10:08
 * Project: Muzi
 * 主界面九宫格Bean
 */

public class FuncGLBean {

    int iconResId = -1;
    String title;

    public FuncGLBean(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
