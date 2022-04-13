package com.emupapps.the_broker.models.register;

import com.emupapps.the_broker.models.ErrorData;
import com.emupapps.the_broker.models.login.response.User;
import com.google.gson.annotations.SerializedName;

public class AuthenticationModelResponse {

    private User user;

    private String jwt;

    private String error;

    @SerializedName("data")
    private ErrorData[] errorData;

    public AuthenticationModelResponse(String jwt) {
        this.jwt = jwt;
    }

    public AuthenticationModelResponse(String error, ErrorData[] errorData) {
        this.error = error;
        this.errorData = errorData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public ErrorData[] getErrorData() {
        return errorData;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
