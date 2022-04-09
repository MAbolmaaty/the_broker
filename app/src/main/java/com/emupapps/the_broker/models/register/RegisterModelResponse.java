package com.emupapps.the_broker.models.register;

import com.emupapps.the_broker.models.login.response.User;
import com.google.gson.annotations.SerializedName;

public class RegisterModelResponse {

    private User user;

    private String jwt;

    public RegisterModelResponse(User user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
