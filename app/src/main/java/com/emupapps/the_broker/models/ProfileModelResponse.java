package com.emupapps.the_broker.models;

public class ProfileModelResponse {
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private ProfilePicture profilePicture;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }
}
