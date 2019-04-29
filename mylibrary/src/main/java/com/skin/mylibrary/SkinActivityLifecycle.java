package com.skin.mylibrary;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import com.skin.mylibrary.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-28
 * Description:
 */
public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private HashMap<Activity, SkinLayoutInflaterFactory> mLayoutInflaterFactory = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        //1. 更新状态栏
        SkinThemeUtils.updateStatusBarColor(activity);

        // 2. 更新布局视图
        // 获得Activity的布局加载器
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        try {
            // Android 布局加载器使用mFactory标记是否设置过Factory
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //使用Factory2 设置布局加载工厂
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);

        // 注册观察者
        SkinManager.getInstance().addObserver(skinLayoutInflaterFactory);
        mLayoutInflaterFactory.put(activity, skinLayoutInflaterFactory);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // 删除观察者
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = mLayoutInflaterFactory.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutInflaterFactory);
    }

    public void updateSkin(Activity activity) {
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = mLayoutInflaterFactory.get(activity);
        skinLayoutInflaterFactory.update(null, null);
    }
}
