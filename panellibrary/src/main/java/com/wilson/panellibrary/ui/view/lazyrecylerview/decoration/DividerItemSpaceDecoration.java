package com.wilson.panellibrary.ui.view.lazyrecylerview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wilson.panellibrary.ui.view.lazyrecylerview.adapter.LazyWrapAdapter;

/**
 * 自定义ItemSpace
 */
public class DividerItemSpaceDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public DividerItemSpaceDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if(null != adapter && adapter instanceof LazyWrapAdapter){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int itemPosition = params.getViewAdapterPosition();

            LazyWrapAdapter lazyWrapAdapter = (LazyWrapAdapter) adapter;
            if(lazyWrapAdapter.isFooter(itemPosition) || lazyWrapAdapter.isHeader(itemPosition)){
                super.getItemOffsets(outRect, view, parent, state);
                return;
            }
        }

        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }
    }
}
