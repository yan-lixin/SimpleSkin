package com.skin.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-28
 * Description:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SkinThemeUtils {

    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr.navigationBarColor};


    public static int[] getResId(Context context, int[] attrs) {

        int[] resIds = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }

    public static void updateStatusBarColor(Activity activity) {
        //5.0以上才能修改
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int[] resIds = getResId(activity, STATUSBAR_COLOR_ATTRS);
        // 修改状态栏的颜色
        // 如果没有配置属性 则获得0
        if (resIds[0] == 0) {
            int statusBarColorId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if (statusBarColorId != 0) {
                activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(statusBarColorId));
            }
        } else {
            activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(resIds[0]));
        }
        //修改底部虚拟按键的颜色
        if (resIds[1] != 0) {
            activity.getWindow().setNavigationBarColor(SkinResources.getInstance().getColor(resIds[1]));
        }
    }
}
