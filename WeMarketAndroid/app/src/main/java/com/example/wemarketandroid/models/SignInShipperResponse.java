package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignInShipperResponse {
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
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
    public SignInShipperResponse() {
    }

    public SignInShipperResponse(Shipper shipper, String tokenType, String accessToken) {
        this.shipper = shipper;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
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
