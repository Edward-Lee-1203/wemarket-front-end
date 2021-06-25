package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInUserResponse {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("type")
    @Expose
    private String tokenType;
    @SerializedName("token")
    @Expose
    private String accessToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public SignInUserResponse() {
    }

    public SignInUserResponse(User user, String tokenType, String accessToken) {
        this.user = user;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
