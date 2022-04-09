package com.emupapps.the_broker.models.request_maintenance.response;

import com.google.gson.annotations.SerializedName;

public class RequestMaintenanceModelResponse {

    @SerializedName("data")
    private String mResult;
    @SerializedName("key")
    private String mKey;

    public String getResult ()
    {
        return mResult;
    }

    public String getKey ()
    {
        return mKey;
    }

}
