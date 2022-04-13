package com.emupapps.the_broker.models;

import com.google.gson.annotations.SerializedName;

public class ErrorData {

    @SerializedName("messages")
    private ErrorMessage [] mErrorMessages;

    public ErrorMessage[] getErrorMessages() {
        return mErrorMessages;
    }
}
