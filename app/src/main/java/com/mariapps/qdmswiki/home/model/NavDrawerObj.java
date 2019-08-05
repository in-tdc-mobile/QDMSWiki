package com.mariapps.qdmswiki.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NavDrawerObj implements Serializable {

    @SerializedName("MenuItemsEntity")
    private List<MenuItemsEntity> menuItemsEntities;

    public List<MenuItemsEntity> getMenuItemsEntities() {
        return menuItemsEntities;
    }

    public void setMenuItemsEntities(List<MenuItemsEntity> menuItemsEntities) {
        this.menuItemsEntities = menuItemsEntities;
    }

    public List<MenuItemsEntity> getMenuList(int parentId) {
        List<NavDrawerObj.MenuItemsEntity> childList = new ArrayList<>();
        if(menuItemsEntities!=null){
            for (NavDrawerObj.MenuItemsEntity pageRightsEntity : menuItemsEntities) {
                if (pageRightsEntity.getParentId() == parentId) {
                    childList.add(pageRightsEntity);
                }
            }
        }

        return childList;
    }

    public static class MenuItemsEntity implements Serializable {
        @SerializedName("IsLeafNode")
        private boolean IsLeafNode;
        @SerializedName("SortOrder")
        private int SortOrder;
        @SerializedName("PageName")
        private String PageName;
        @SerializedName("PageHeaderName")
        private String PageHeaderName;
        @SerializedName("DisplayName")
        private String DisplayName;
        @SerializedName("ParentId")
        private int ParentId;
        @SerializedName("Id")
        private int Id;
        @SerializedName("Type")
        private String Type;

        public MenuItemsEntity(int id, int parentId, String pageName, String pageHeaderName, String displayName, boolean isLeafNode, int sortOrder, String type) {
            IsLeafNode = isLeafNode;
            SortOrder = sortOrder;
            PageName = pageName;
            PageHeaderName = pageHeaderName;
            DisplayName = displayName;
            ParentId = parentId;
            Id = id;
            Type = type;
        }

        public boolean getIsLeafNode() {
            return IsLeafNode;
        }

        public int getSortOrder() {
            return SortOrder;
        }

        public String getPageName() {
            return PageName;
        }

        public String getPageHeaderName() {
            return PageHeaderName;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public int getParentId() {
            return ParentId;
        }

        public int getId() {
            return Id;
        }

        public String getType() {
            return Type;
        }
    }


}

