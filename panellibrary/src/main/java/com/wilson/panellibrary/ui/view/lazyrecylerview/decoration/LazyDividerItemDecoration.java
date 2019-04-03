package com.wilson.panellibrary.ui.view.lazyrecylerview.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.wilson.panellibrary.ui.view.lazyrecylerview.adapter.LazyWrapAdapter;

import java.util.List;

/**
 * 自定义ItemDecoration分割线，支持List,Grid,StaggerGrid
 *
 * @Author：weizhaosheng@aliyun.com
 * @date: 2019年04月03日
 */
public class LazyDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private boolean isHeaderDividerEnable = false;
    private boolean isFooterDividerEnable = false;
    private boolean isItemMarginAverage = true;

    private Drawable mDivider = null;
    private int mDividerHeight = 0;
    private int mItemSpace = 0;

    public LazyDividerItemDecoration(Context context, Drawable divider, int dividerHeight, boolean isNeedDivider) {
        if (null == divider && isNeedDivider) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();

            this.mDividerHeight = 1;
        } else {
            this.mDivider = divider;
            this.mDividerHeight = dividerHeight < 0 ? 0 : dividerHeight;
        }
    }

    public void setItemMarginAverage(boolean isItemMarginAverage) {
        this.isItemMarginAverage = isItemMarginAverage;
    }

    public void setHeaderDividerEnable(boolean headerDividerEnable) {
        isHeaderDividerEnable = headerDividerEnable;
    }

    public void setFooterDividerEnable(boolean footerDividerEnable) {
        isFooterDividerEnable = footerDividerEnable;
    }

    public void setmDivider(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    public void setmDividerHeight(int mDividerHeight) {
        this.mDividerHeight = mDividerHeight;
    }

    public void setmItemSpace(int mItemSpace) {
        this.mItemSpace = mItemSpace;
    }

    private int getDividerHeight() {
        return mDividerHeight;
    }

    private int getItemSpace() {
        return mItemSpace;
    }

    /**
     * 获取Grid/StaggerGrid的列数
     *
     * @param layoutManager
     * @return
     */
    private int getSpanCount(RecyclerView.LayoutManager layoutManager) {
        // 列数
        int spanCount = -1;
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * Header个数
     *
     * @param adapter
     * @return
     */
    private int getHeaderCount(RecyclerView.Adapter adapter) {
        if (adapter instanceof LazyWrapAdapter) {
            return ((LazyWrapAdapter) adapter).getHeadersCount();
        }
        return 0;
    }

    /**
     * Footer个数
     *
     * @param adapter
     * @return
     */
    private int getFooterCount(RecyclerView.Adapter adapter) {
        if (adapter instanceof LazyWrapAdapter) {
            return ((LazyWrapAdapter) adapter).getFootersCount();
        }
        return 0;
    }

    /**
     * 是否是Header
     *
     * @param adapter
     * @param itemPosition
     * @return
     */
    private boolean isHeaderPoistion(RecyclerView.Adapter adapter, int itemPosition, View child) {
        if (adapter instanceof LazyWrapAdapter) {
            List<View> views = ((LazyWrapAdapter) adapter).getmHeaderViews();
            for (View view : views) {
                if (view == child) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是Footer
     *
     * @param adapter
     * @param itemPosition
     * @return
     */
    private boolean isFooterPosition(RecyclerView.Adapter adapter, int itemPosition, View child) {
        if (adapter instanceof LazyWrapAdapter) {
            List<View> views = ((LazyWrapAdapter) adapter).getmFootViews();
            for (View view : views) {
                if (view == child) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final RecyclerView.Adapter adapter = parent.getAdapter();
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (getItemSpace() > 0) {
                return;
            }
            drawGridLayoutHorizontal(adapter, c, parent);
            drawGridLayoutVertical(adapter, c, parent);
            return;
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (getItemSpace() > 0) {
                return;
            }
            return;
        }

        if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                drawLinearLayoutHorizontal(adapter, c, parent);
            } else {
                drawLinearLayoutVertical(adapter, c, parent);
            }
            return;
        }
    }

    /**
     * 绘制LienarLayout横线
     *
     * @param adapter
     * @param c
     * @param parent
     */
    private void drawLinearLayoutHorizontal(RecyclerView.Adapter adapter, Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (isHeaderPoistion(adapter, i, child) && !isHeaderDividerEnable) {
                continue;
            }

            if (isFooterPosition(adapter, i, child) && !isFooterDividerEnable) {
                continue;
            }

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + getDividerHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 绘制LienarLayout竖线
     *
     * @param adapter
     * @param c
     * @param parent
     */
    private void drawLinearLayoutVertical(RecyclerView.Adapter adapter, Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (isHeaderPoistion(adapter, i, child) && !isHeaderDividerEnable) {
                continue;
            }

            if (isFooterPosition(adapter, i, child) && !isFooterDividerEnable) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + getDividerHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    /**
     * 绘制GridLayout竖向
     *
     * @param adapter
     * @param c
     * @param parent
     */
    private void drawGridLayoutVertical(RecyclerView.Adapter adapter, Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            if (isHeaderPoistion(adapter, i, child) && !isHeaderDividerEnable) {
                continue;
            }

            if (isFooterPosition(adapter, i, child) && !isFooterDividerEnable) {
                continue;
            }

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + getDividerHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 绘制GridLayout横向
     *
     * @param adapter
     * @param c
     * @param parent
     */
    private void drawGridLayoutHorizontal(RecyclerView.Adapter adapter, Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (isHeaderPoistion(adapter, i, child) && !isHeaderDividerEnable) {
                continue;
            }

            if (isFooterPosition(adapter, i, child) && !isFooterDividerEnable) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + getDividerHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (null == mDivider) {
            return;
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = params.getViewAdapterPosition();

        if (isHeaderPoistion(adapter, itemPosition, view) && !isHeaderDividerEnable) {
            return;
        }

        if (isFooterPosition(adapter, itemPosition, view) && !isFooterDividerEnable) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int childCount = parent.getAdapter().getItemCount();

        if (layoutManager instanceof GridLayoutManager) {
            handGridLayoutItemOffsets(adapter, (GridLayoutManager) layoutManager, itemPosition, childCount, outRect, view, state);
            return;
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            handStaggerGridLayoutItemOffsets(parent,adapter, (StaggeredGridLayoutManager) layoutManager, itemPosition, childCount, outRect, view, state);
            return;
        }

        if (layoutManager instanceof LinearLayoutManager) {
            handLinearLayoutItemOffsets(adapter, (LinearLayoutManager) layoutManager, itemPosition, childCount, outRect, view, state);
            return;
        }

    }

    /**
     * 处理LinearLayout
     *
     * @param adapter
     * @param layoutManager
     * @param itemPosition
     * @param childCount
     * @param outRect
     * @param view
     * @param state
     */
    private void handLinearLayoutItemOffsets(RecyclerView.Adapter adapter, LinearLayoutManager layoutManager, int itemPosition, int childCount, Rect outRect, View view, RecyclerView.State state) {
        int orientation = layoutManager.getOrientation();
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, getDividerHeight());
        } else {
            outRect.set(0, 0, getDividerHeight(), 0);
        }
    }


    /**
     * 处理StaggerLayout
     *
     * @param parent
     * @param adapter
     * @param layoutManager
     * @param itemPosition
     * @param childCount
     * @param outRect
     * @param view
     * @param state
     */
    private void handStaggerGridLayoutItemOffsets(RecyclerView parent, RecyclerView.Adapter adapter, StaggeredGridLayoutManager layoutManager, int itemPosition, int childCount, Rect outRect, View view, RecyclerView.State state) {
        if (getItemSpace() <= 0) {
            return;
        }
        int spanCount = getSpanCount(layoutManager);

        outRect.left = getItemSpace();
        outRect.bottom = getItemSpace();
        if (isFirstRow(adapter,layoutManager,itemPosition,spanCount,childCount)) {
            outRect.top = getItemSpace();
        }

        if(isItemMarginAverage){
            //布局的itemPosition
            itemPosition = itemPosition == 0 ? itemPosition:itemPosition-getHeaderCount(adapter);
            if(isLastColum(layoutManager,itemPosition,spanCount,childCount)){
                outRect.right = getItemSpace();
            }
        }else{
            outRect.right = getItemSpace();
        }
    }


    /**
     * 处理GridLayout
     *
     * @param adapter
     * @param layoutManager
     * @param itemPosition
     * @param childCount
     * @param outRect
     * @param view
     * @param state
     */
    private void handGridLayoutItemOffsets(RecyclerView.Adapter adapter, GridLayoutManager layoutManager, int itemPosition, int childCount, Rect outRect, View view, RecyclerView.State state) {
        int spanCount = getSpanCount(layoutManager);
        if(getItemSpace() > 0){
            outRect.left = getItemSpace();
            outRect.right = getItemSpace();
            outRect.bottom = getItemSpace();

            if (isFirstRow(adapter,layoutManager,itemPosition,spanCount,childCount)) {
                outRect.top = getItemSpace();
            }

            //TODO 未解决的部分判断最后一列
            /*if(isItemMarginAverage){
                itemPosition = itemPosition == 0 ? itemPosition:itemPosition-getHeaderCount(adapter);
                if(isLastColum(layoutManager,itemPosition,spanCount,childCount)){
                    outRect.right = getItemSpace();
                }
            }else{
                outRect.right = getItemSpace();
            }*/
        }else{
            if (isLastRaw(layoutManager, itemPosition + getHeaderCount(adapter), spanCount, childCount)) {
                // 如果是最后一行，则不需要绘制底部
                outRect.set(0, 0, getDividerHeight(), 0);
            } else if (isLastColum(layoutManager, itemPosition + getHeaderCount(adapter), spanCount, childCount)) {
                // 如果是最后一列，则不需要绘制右边
                outRect.set(0, 0, 0, getDividerHeight());
            } else {
                outRect.set(0, 0, getDividerHeight(), getDividerHeight());
            }
        }
    }

    /**
     * 最后一列
     *
     * @param layoutManager
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastColum(RecyclerView.LayoutManager layoutManager, int pos, int spanCount, int childCount) {
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是第一行
     * @param layoutManager
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isFirstRow(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, int pos, int spanCount, int childCount){
        return pos >= getHeaderCount(adapter) && pos <= spanCount;
    }

    /**
     * 最后一行
     *
     * @param layoutManager
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRaw(RecyclerView.LayoutManager layoutManager, int pos, int spanCount, int childCount) {
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount) {// 如果是最后一行，则不需要绘制底部
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount) {
                    return true;
                }
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

}
