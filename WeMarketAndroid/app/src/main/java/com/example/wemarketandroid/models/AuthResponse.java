package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;
    @SerializedName("tokenType")
    @Expose
    private String tokenType;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public AuthResponse() {
    }

    /**
     *
     * @param roles
     * @param id
     * @param tokenType
     * @param accessToken
     * @param username
     */
    public AuthResponse(Integer id, String username, List<String> roles, String tokenType, String accessToken) {
        super();
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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
