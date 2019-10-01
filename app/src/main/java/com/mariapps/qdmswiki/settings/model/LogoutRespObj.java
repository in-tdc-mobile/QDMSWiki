package com.mariapps.qdmswiki.settings.model;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.commonmodels.CommonEntity;

/**
 * Created by elby.samson on 29,March,2019
 */
public class LogoutRespObj {

    @SerializedName("CommonEntity")
    private CommonEntity commonEntity;

    public CommonEntity getCommonEntity() {
        return commonEntity;
    }
}
