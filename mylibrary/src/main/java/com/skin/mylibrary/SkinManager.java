package com.skin.mylibrary;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.skin.mylibrary.utils.SkinPreference;
import com.skin.mylibrary.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-28
 * Description:
 */
public class SkinManager extends Observable {

    private static SkinManager mInstance;
    private Application mApplication;
    private SkinActivityLifecycle mSkinActivityLifecycle;

    private SkinManager(Application application) {
        mApplication = application;
        // 共享首选项 用于记录当前使用的皮肤
        SkinPreference.init(application);
        // Skin下的资源
        SkinResources.init(application);
        // 注册Activity生命周期回调
        mSkinActivityLifecycle = new SkinActivityLifecycle();
        application.registerActivityLifecycleCallbacks(mSkinActivityLifecycle);

        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public static void init(Application application) {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    /**
     * 加载皮肤插件、换肤
     * @param skinPath
     */
    public void loadSkin(String skinPath) {
        // 还原默认皮肤
        if (TextUtils.isEmpty(skinPath)) {
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();
        } else {

            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                // 添加资源到资源管理器
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, skinPath);

                Resources appResources = mApplication.getResources();
                Resources skinResource = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());

                // 获取Skin插件的包名
                PackageManager pm = mApplication.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = packageInfo.packageName;
                SkinResources.getInstance().applySkin(skinResource, packageName);

                // 保存当前使用的SKin插件
                SkinPreference.getInstance().setSkin(skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers();
    }

    public void updateSkin(Activity activity) {
        mSkinActivityLifecycle.updateSkin(activity);
    }

}
