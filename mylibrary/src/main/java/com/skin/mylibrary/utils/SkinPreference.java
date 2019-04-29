package com.skin.mylibrary.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-28
 * Description:
 */
public class SkinPreference {

    private static final String SKIN_SHARED = "skins";
    private static final String KEY_SKIN_PATH = "skin-path";

    private static SkinPreference mInstance;

    private final SharedPreferences mSharedPreferences;

    private SkinPreference(Context context) {
        mSharedPreferences = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (SkinPreference.class) {
                if (mInstance == null) {
                    mInstance = new SkinPreference(context);
                }
            }
        }
    }

    public static SkinPreference getInstance() {
        return mInstance;
    }

    public void setSkin(String skinPath) {
        mSharedPreferences.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    public String getSkin() {
        return mSharedPreferences.getString(KEY_SKIN_PATH, null);
    }
}
