package com.skin.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skin.mylibrary.SkinManager;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-29
 * Description:
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().updateSkin(this);
    }
}
