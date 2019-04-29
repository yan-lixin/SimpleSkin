package com.skin.sample;

import android.app.Application;

import com.skin.mylibrary.SkinManager;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-29
 * Description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
