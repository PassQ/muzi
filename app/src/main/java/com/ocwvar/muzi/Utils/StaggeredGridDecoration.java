package com.ocwvar.muzi.Utils;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Utils
 * Date: 2016/5/4  20:50
 * Project: Muzi
 * 用于使主界面宫格列表居中显示
 */

public class StaggeredGridDecoration extends RecyclerView.ItemDecoration {

    private int left;

    private boolean needInit = true;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        view.measure(9999, 9999);
        if (needInit && view.getMeasuredWidth() > 0) {
            init(view, parent);
        }
        outRect.left = left;
        outRect.top = 10;
        outRect.bottom = 10;
    }

    private void init(View view, RecyclerView parent) {
        needInit = false;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int count = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            this.left = (parent.getMeasuredWidth() - view.getMeasuredWidth() * count) / 2 / count;
        }
    }
}
