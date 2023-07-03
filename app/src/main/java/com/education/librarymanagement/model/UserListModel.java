package com.education.librarymanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Model Class to hold User List
 * */
public class UserListModel {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<UserModel> userDetail;

    // Constructor to initialize member variables to its default
    public UserListModel() {
        status = null;
        userDetail = null;
    }

    public UserListModel(String status, List<UserModel> userDetail) {
        this.status = status;
        this.userDetail = userDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserModel> getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(List<UserModel> userDetail) {
        this.userDetail = userDetail;
    }
}