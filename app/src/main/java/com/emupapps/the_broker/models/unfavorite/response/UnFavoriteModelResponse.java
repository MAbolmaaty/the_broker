package com.emupapps.the_broker.models.unfavorite.response;

import com.google.gson.annotations.SerializedName;

public class UnFavoriteModelResponse {

    @SerializedName("message")
    private String mResult;

    private String key;

    public String getResult() {
        return mResult;
    }

    public String getKey() {
        return key;
    }
}
