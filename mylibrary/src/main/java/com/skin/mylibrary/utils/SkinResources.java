package com.skin.mylibrary.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-29
 * Description:
 */
public class SkinResources {
    private static SkinResources mInstance;

    /**
     * Skin插件的Resources
     */
    private Resources mSkinResources;
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;

    /**
     * 当前App下的Resources
     */
    private Resources mAppResources;

    private SkinResources(Context context) {
        mAppResources = context.getResources();
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (SkinResources.class) {
                if (mInstance == null) {
                    mInstance = new SkinResources(context);
                }
            }
        }
    }

    public static SkinResources getInstance() {
        return mInstance;
    }

    public void applySkin(Resources resources, String pkgName) {
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        // 是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    public int getIdentifier(int resId) {
        // 返回当前APP的资源id
        if (isDefaultSkin) {
            return resId;
        }

        // 对于Skin插件的id，因为Resources不同，需要先获取当前APP下的资源id对应的名字与类型，再据此获取到Skin插件的资源id
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinResId = mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinResId;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是Drawable
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }

    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return mAppResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return mAppResources.getString(skinId);
            }
            return mSkinResources.getString(skinId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
