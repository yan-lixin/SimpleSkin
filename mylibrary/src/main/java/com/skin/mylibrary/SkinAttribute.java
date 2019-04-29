package com.skin.mylibrary;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skin.mylibrary.utils.SkinResources;
import com.skin.mylibrary.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-28
 * Description:
 */
class SkinAttribute {
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");

        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }


    List<SkinView> mSkinViews = new ArrayList<>();

    public void load(View view, AttributeSet attrs) {
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            // 获得属性名
            String attributeName = attrs.getAttributeName(i);
            // 检测是否符合需要筛选的属性名
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                // 如果定义为十六进制颜色值，则不对其进行操作
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                //资源id
                int resId;
                if (attributeValue.startsWith("?")) {
                    //attr Id
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    //获得主题style中的对应attr的资源id值
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    // @12343455332
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    //可以被替换的属性
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }
            }
        }

        //将View与之对应的可以动态替换的属性集合 放入 集合中
        if (!skinPairs.isEmpty() || view instanceof TextView || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin();
            mSkinViews.add(skinView);
        }
    }

    /**
     * 换皮肤
     */
    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin();
        }
    }

    static class SkinView {
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin() {
            applySkinViewSupport();
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair
                                .resId);
                        //Color
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair
                                .resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList
                                (skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }

        private void applySkinViewSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }

    }

    static class SkinPair {

        String attributeName;
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
