package com.emupapps.the_broker.models.document_delete;

import com.google.gson.annotations.SerializedName;

public class DeleteDocumentModelResponse {

    @SerializedName("mesaage")
    private String result;

    private String key;

    public String getResult() {
        return result;
    }

    public String getKey() {
        return key;
    }
}
