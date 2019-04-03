package com.wilson.panellibrary.ui.view.lazyrecylerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.panellibrary.base.adapter.BaseRecyclerAdapter;
import com.wilson.panellibrary.base.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * 自定义添加Header和Footer的适配器
 *
 * @author weizhaosheng@aliyun.com
 * @date 2019年04月03日
 */
public class LazyWrapAdapter<T> extends Adapter<BaseViewHolder> {
    final ArrayList<View> EMPTY_INFO_LIST = new ArrayList<View>();

    private Adapter<ViewHolder> mAdapter;
    private ArrayList<View> mHeaderViews;
    private ArrayList<View> mFootViews;

    private int headerPosition = 0;

    public LazyWrapAdapter(ArrayList<View> mHeaderViews, ArrayList<View> mFootViews, Adapter<ViewHolder> mAdapter) {
        this.mAdapter = mAdapter;
        this.mAdapter.registerAdapterDataObserver(getObserver());

        this.mHeaderViews = null == mFootViews ? EMPTY_INFO_LIST : mHeaderViews;
        this.mFootViews = null == mFootViews ? EMPTY_INFO_LIST : mFootViews;
    }

    public ArrayList<View> getmHeaderViews() {
        return mHeaderViews;
    }

    public ArrayList<View> getmFootViews() {
        return mFootViews;
    }

    /**
     * 当前布局是否为Header
     *
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return position >= 0 && position < mHeaderViews.size();
    }

    /**
     * 当前布局是否为Footer
     *
     * @param position
     * @return
     */
    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - mFootViews.size();
    }

    /**
     * Header的数量
     *
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * Footer的数量
     *
     * @return
     */
    public int getFootersCount() {
        return mFootViews.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerView.INVALID_TYPE) {
            return new HeaderFooterViewHolder(mHeaderViews.get(headerPosition++));
        } else if (viewType == RecyclerView.INVALID_TYPE - 1) {
            return new HeaderFooterViewHolder(mFootViews.get(0));
        }

        if (mAdapter instanceof BaseRecyclerAdapter) {
            return ((BaseRecyclerAdapter) mAdapter).onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return;
        }
        int adjPosition = position - numHeaders;
        int adapterCount;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return RecyclerView.INVALID_TYPE;
        }
        int adjPosition = position - numHeaders;
        int adapterCount;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        return RecyclerView.INVALID_TYPE - 1;
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        int numFooters = getFootersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }


    /**
     * HeaderFooterViewHolder
     *
     * @author cheng.li@dchuan.com
     * @date 2015年8月17日
     */
    private class HeaderFooterViewHolder extends BaseViewHolder {
        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private RecyclerView.AdapterDataObserver getObserver() {
        return new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int count = getHeadersCount();
                notifyItemRangeInserted(positionStart + count, itemCount);
                notifyItemRangeChanged(positionStart + count + itemCount - 1, getItemCount() - itemCount - positionStart - getFootersCount());
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                int count = getHeadersCount();
                notifyItemRangeRemoved(count + positionStart, itemCount);
                notifyItemRangeChanged(count + positionStart, getItemCount() - count - positionStart - getFootersCount());
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                // TODO:
            }
        };
    }
}
