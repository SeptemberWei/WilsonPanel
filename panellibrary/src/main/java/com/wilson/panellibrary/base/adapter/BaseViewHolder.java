package com.wilson.panellibrary.base.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.panellibrary.utils.ViewUtils;

/**
 * @author weizhaosheng@aliyun.com
 * @Date 2019年03月15日11:02:47
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View mConvertView;
    private SparseArray<View> mViews = new SparseArray<>();


    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mConvertView = itemView;
    }

    public BaseViewHolder(int viewType, View convertView, ViewGroup viewGroup) {
        super(convertView);
        this.mConvertView = convertView;
    }

    /**
     * 获取item的根控件
     *
     * @return
     */
    public View getmConvertView() {
        return mConvertView;
    }

    /**
     * 根据Id获取View
     *
     * @param id
     * @return
     */
    public <T extends View> T getView(int id) {
        View v = (View) this.mViews.get(id);
        if (null == v) {
            v = ViewUtils.findViewById(mConvertView, id);
            this.mViews.put(id, v);
        }
        return (T) v;
    }

    /**
     * 设置view的点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickLister(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view instanceof TextView || view instanceof ViewGroup) {
            view.setClickable(true);
        }
        view.setOnClickListener(listener);
    }

    /**
     * 设置view的长按事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnLongClickLister(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view instanceof TextView || view instanceof ViewGroup) {
            view.setClickable(true);
        }
        view.setOnLongClickListener(listener);
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param text
     * @return
     */
    public void setText(@IdRes int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param stringResId 字符串资源id
     * @return
     */
    public void setText(@IdRes int viewId, @StringRes int stringResId) {
        TextView view = getView(viewId);
        view.setText(stringResId);
    }

    /**
     * 设置对应id的控件的html文本内容
     *
     * @param viewId
     * @param source html文本
     * @return
     */
    public void setHtml(int viewId, String source) {
        TextView view = getView(viewId);
        view.setText(Html.fromHtml(source));
    }

    /**
     * 设置对应id的控件是否选中
     *
     * @param viewId
     * @param checked
     * @return
     */
    public void setChecked(int viewId, boolean checked) {
        CompoundButton view = getView(viewId);
        view.setChecked(checked);
    }

    /**
     * 设置view的enabled属性
     *
     * @param viewId
     * @param enabled
     */
    public void setEnabled(int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
    }

    /**
     * 设置控件显示隐藏
     *
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
    }

    /**
     * 设置图片
     *
     * @param viewId
     * @param bitmap
     */
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    /**
     * 设置图片level
     *
     * @param viewId
     * @param level
     */
    public void setImageLevel(int viewId, int level) {
        ImageView view = getView(viewId);
        view.setImageLevel(level);
    }

    /**
     * 设置Drawable
     *
     * @param viewId
     * @param drawable
     */
    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
    }

    /**
     * 设置DrawableRes
     *
     * @param viewId
     * @param drawableRes
     */
    public void setImageResource(int viewId, int drawableRes) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableRes);
    }

    /**
     * 设置字体颜色
     *
     * @param viewId
     * @param textColor 颜色值
     * @return
     */
    public void setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
    }

    /**
     * 设置背景资源
     *
     * @param viewId
     * @param backgroundResId 背景资源id
     * @return
     */
    public void setBackgroundRes(int viewId, int backgroundResId) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundResId);
    }

    /**
     * 设置背景色
     *
     * @param viewId
     * @param color  颜色值
     * @return
     */
    public void setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
    }

}
