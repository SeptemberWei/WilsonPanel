package com.wilson.panellibrary.utils;

import android.support.annotation.IdRes;
import android.view.View;

public class ViewUtils {
    public static <T extends View> T findViewById(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }
}
