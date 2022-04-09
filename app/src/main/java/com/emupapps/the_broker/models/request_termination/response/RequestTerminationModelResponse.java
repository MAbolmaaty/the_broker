package com.emupapps.the_broker.models.request_termination.response;

import com.google.gson.annotations.SerializedName;

public class RequestTerminationModelResponse {

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
