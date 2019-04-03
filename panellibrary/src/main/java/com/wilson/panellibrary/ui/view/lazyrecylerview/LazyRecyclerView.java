package com.wilson.panellibrary.ui.view.lazyrecylerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.panellibrary.R;
import com.wilson.panellibrary.ui.view.lazyrecylerview.adapter.LazyWrapAdapter;
import com.wilson.panellibrary.ui.view.lazyrecylerview.decoration.LazyDividerItemDecoration;

import java.util.ArrayList;

public class LazyRecyclerView extends RecyclerView implements Runnable {

    private static final int LAYOUT_MANAGER_TYPE_LINEAR = 0;
    private static final int LAYOUT_MANAGER_TYPE_GRID = 1;
    private static final int LAYOUT_MANAGER_TYPE_STAGGERED_GRID = 2;

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    /**
     * 分割线默认高度
     */
    private static final int DIVIDER_HEIGHT = 1;
    private static final int DEFAUTL_SPANCOUNT = 2;

    private final ArrayList<View> mHeaderViews = new ArrayList<View>();
    private final ArrayList<View> mFootViews = new ArrayList<View>();

    /**
     * 分割线
     */
    private Drawable mdivider;
    /**
     * 分割线高度/宽度
     */
    private int mDividerHeight;

    /**
     * Grid/StaggerGrid的列数
     */
    private int mSpanCount;
    private int mItemMargin;

    /**
     * ItemMargin间距一致
     */
    private boolean isItemMarginAverage = true;

    /**
     * 布局类型
     */
    private int mLayoutManagerType;
    /**
     * 布局方向
     */
    private int mLayoutManagerOriention;

    /**
     * 是否支持Header分割线
     */
    private boolean isHeaderDividerEnable = false;
    /**
     * 是否支持Footer分割线
     */
    private boolean isFooterDividerEnable = false;
    private boolean isReverseLayout = false;

    private LazyDividerItemDecoration mLazyDividerItemDecoration;

    public LazyRecyclerView(@NonNull Context context) {
        super(context);
        initLayout(context, null);
    }

    public LazyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public LazyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LazyRecyclerView);
        boolean isNeedDivider = true;
        Drawable divider = null;
        if (typedArray.hasValue(R.styleable.LazyRecyclerView_lrv_divider)) {
            divider = typedArray.getDrawable(R.styleable.LazyRecyclerView_lrv_divider);
            if (null == divider) {
                isNeedDivider = false;
            }
        }

        int dividerHeight = DIVIDER_HEIGHT;
        if (typedArray.hasValue(R.styleable.LazyRecyclerView_lrv_dividerHeight)) {
            dividerHeight = (int) typedArray.getDimension(R.styleable.LazyRecyclerView_lrv_dividerHeight, -1);
            if (dividerHeight <= 0) {
                isNeedDivider = false;
                dividerHeight = 0;
            }
        }

        mSpanCount = typedArray.getInt(R.styleable.LazyRecyclerView_lrv_spanCount, DEFAUTL_SPANCOUNT);

        isReverseLayout = typedArray.getBoolean(R.styleable.LazyRecyclerView_lrv_reverseLayout, false);
        isHeaderDividerEnable = typedArray.getBoolean(R.styleable.LazyRecyclerView_lrv_headerDividerEnable, false);
        isFooterDividerEnable = typedArray.getBoolean(R.styleable.LazyRecyclerView_lrv_footerDividerEnable, false);

        isItemMarginAverage = typedArray.getBoolean(R.styleable.LazyRecyclerView_lrv_itemMarginAverage, true);

        mItemMargin = (int) typedArray.getDimension(R.styleable.LazyRecyclerView_lrv_itemMargin, 0);
        mLayoutManagerType = typedArray.getInt(R.styleable.LazyRecyclerView_lrv_layoutManager, LAYOUT_MANAGER_TYPE_LINEAR);
        mLayoutManagerOriention = typedArray.getInt(R.styleable.LazyRecyclerView_lrv_layoutManagerOriention, VERTICAL);
        typedArray.recycle();
        switch (mLayoutManagerType) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                setLinearLayoutManager(mLayoutManagerOriention, isReverseLayout);
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                setGridLayout(mSpanCount, mLayoutManagerOriention, isReverseLayout);
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                setStaggerGridLayout(mSpanCount, mLayoutManagerOriention);
                break;
        }
        setDivider(divider, dividerHeight, mLayoutManagerOriention, isNeedDivider);
        post(this);
    }

    /**
     * 如果Item高度固定可设置为true，提高性能
     *
     * @param isFixed
     */
    public void setLazyHasFixedSize(boolean isFixed) {
        this.setHasFixedSize(isFixed);
    }

    /**
     * 设置线性布局管理器
     *
     * @param orientation 0 横，1竖
     */
    public void setLinearLayoutManager(int orientation, boolean isReverse) {
        this.setLayoutManager(new LinearLayoutManager(getContext(), orientation, isReverse));
    }

    /**
     * 设置Grid布局管理器
     *
     * @param spanCount
     * @param orientation 0 横，1竖
     */
    public void setGridLayout(int spanCount, int orientation, boolean isReverse) {
        this.setLayoutManager(new GridLayoutManager(getContext(), spanCount, orientation, false));
    }

    /**
     * 设置瀑布流布局管理器
     *
     * @param spanCount
     * @param orientation 0 横，1竖
     */
    public void setStaggerGridLayout(int spanCount, int orientation) {
        this.setLayoutManager(new StaggeredGridLayoutManager(spanCount, orientation));
    }


    /**
     * 设置分割线
     *
     * @param divider
     * @param dividerHeight
     * @param mLayoutManagerOriention
     * @param isNeedDivider
     */
    private void setDivider(Drawable divider, int dividerHeight, int mLayoutManagerOriention, boolean isNeedDivider) {
        this.mdivider = divider;
        this.mDividerHeight = dividerHeight;
        mLazyDividerItemDecoration = new LazyDividerItemDecoration(getContext(), this.mdivider, this.mDividerHeight, isNeedDivider);
        mLazyDividerItemDecoration.setmItemSpace(this.mItemMargin);
        mLazyDividerItemDecoration.setItemMarginAverage(this.isItemMarginAverage);
        mLazyDividerItemDecoration.setHeaderDividerEnable(this.isHeaderDividerEnable);
        mLazyDividerItemDecoration.setFooterDividerEnable(this.isFooterDividerEnable);
        this.addItemDecoration(mLazyDividerItemDecoration);
    }

    public void setmDividerHeight(int mDividerHeight) {
        this.mDividerHeight = mDividerHeight;
        mLazyDividerItemDecoration.setmDividerHeight(mDividerHeight);
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setMdivider(Drawable mdivider) {
        this.mdivider = mdivider;
        mLazyDividerItemDecoration.setmDivider(this.mdivider);
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setFooterDividerEnable(boolean footerDividerEnable) {
        this.isFooterDividerEnable = footerDividerEnable;
        mLazyDividerItemDecoration.setFooterDividerEnable(this.isFooterDividerEnable);
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setHeaderDividerEnable(boolean headerDividerEnable) {
        this.isHeaderDividerEnable = headerDividerEnable;
        mLazyDividerItemDecoration.setFooterDividerEnable(this.isHeaderDividerEnable);
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setmItemMargin(int mItemMargin) {
        this.mItemMargin = mItemMargin;
        mLazyDividerItemDecoration.setmItemSpace(this.mItemMargin);
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setmSpanCount(int mSpanCount) {
        this.mSpanCount = mSpanCount;
    }

    /**
     * 添加Header视图
     *
     * @param view
     */
    public void addHeaderView(View view) {
        mHeaderViews.add(view);
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 移除所有的Header视图
     */
    public void clearHeaderView() {
        mHeaderViews.clear();
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 添加Footer视图
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (mFootViews.indexOf(view) > 0) {
            return;
        }
        mFootViews.add(view);
        if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            view.setLayoutParams(layoutParams);
        }
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 移除Footer视图
     */
    public void removeFooterView() {
        mFootViews.clear();
        if (null != getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        // 使用包装了头部和脚部的适配器
        LazyWrapAdapter layzWrapAdapter = new LazyWrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(layzWrapAdapter);
        post(this);
    }

    @Override
    public void run() {
        LayoutManager manager = getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            layoutGridAttach((GridLayoutManager) manager);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            layoutStaggeredGridHeadAttach((StaggeredGridLayoutManager) manager);
        }
    }

    /**
     * 给StaggeredGridLayoutManager附加头部和滑动过度监听
     *
     * @param manager
     */
    private void layoutStaggeredGridHeadAttach(StaggeredGridLayoutManager manager) {
        Adapter mAdapter = getAdapter();
        if (null == mAdapter) {
            return;
        }

        // 从前向后查找Header并设置为充满一行
        View view = null;
        int count = mAdapter.getItemCount();
        for (int i = 0; i < count; i++) {
            if (((LazyWrapAdapter) mAdapter).isHeader(i)) {
                view = getChildAt(i);
                ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).setFullSpan(true);
                view.requestLayout();
            } else {
                break;
            }
        }
    }

    /**
     * 给GridLayoutManager附加头部脚部和滑动过度监听
     *
     * @param manager
     */
    private void layoutGridAttach(final GridLayoutManager manager) {
        final Adapter mAdapter = getAdapter();
        if (null == mAdapter) {
            return;
        }
        // GridView布局
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return ((LazyWrapAdapter) mAdapter).isHeader(position)
                        || ((LazyWrapAdapter) mAdapter).isFooter(position) ? manager.getSpanCount() : 1;
            }
        });
        requestLayout();
    }
}
