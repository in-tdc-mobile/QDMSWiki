package com.mariapps.qdmswiki.settings.model;

/**
 * Created by elby.samson on 21,February,2019
 */
public class SettingsItem {
    private int image;
    private String name;
    private int textColor;

    public SettingsItem(int image, String name, int textColor) {
        this.image = image;
        this.name = name;
        this.textColor = textColor;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getTextColor() {
        return textColor;
    }
}
