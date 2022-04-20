package com.emupapps.the_broker.models;

import android.graphics.drawable.Drawable;

public class SettingItem {
    private final Drawable mIcon;
    private final String mTitle;
    private final String mDescription;

    public SettingItem(Drawable icon, String title, String description) {
        mIcon = icon;
        mTitle = title;
        mDescription = description;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }
}
