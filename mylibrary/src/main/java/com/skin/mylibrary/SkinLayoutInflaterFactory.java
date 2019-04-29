package com.skin.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.skin.mylibrary.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-28
 * Description:
 */
class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {
    private static final String TAG = SkinLayoutInflaterFactory.class.getSimpleName();

    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.view."
    };

    // 记录对应View的构造函数
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();
    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class
    };

    // 当选择新皮肤后需要替换View与之对应的属性
    // 页面属性管理器
    private SkinAttribute mSkinAttribute;

    private Activity mActivity;

    public SkinLayoutInflaterFactory(Activity activity) {
        mActivity = activity;
        mSkinAttribute = new SkinAttribute();
    }

    /**
     * @param parent  当前父布局
     * @param name    名字：TextView、Button等
     * @param context 上下文
     * @param attrs   属性：android:text等
     * @return null则由系统创建
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 换肤就是在需要的时候替换View的属性
        // 创建View，从而修改View的属性
        View view = createViewFromTag(name, context, attrs);
        if (null == view) {
            view = createView(name, context, attrs);
        }

        if (null != view) {
            Log.e(TAG, String.format("[%s]:%s", context.getClass().getName(), name));
            // 加载属性、筛选需要换肤的View
            mSkinAttribute.load(view, attrs);
        }
        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        // 如果包含 . 则不是SDK中的View 可能是自定义View或者support、supportx中的View
        if (name.contains(".")) {
            return null;
        }

        //不包括就要在解析的节点name前拼接上路径, 然后尝试去反射
        View view = null;
        for (String s : mClassPrefixList) {
            view = createView(s + name, context, attrs);
            if (null != view) {
                return view;
            }
        }

        return null;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != constructor) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBarColor(mActivity);
        mSkinAttribute.applySkin();
    }
}
