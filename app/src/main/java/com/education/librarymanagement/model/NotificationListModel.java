package com.education.librarymanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Model Class to hold Notifications List
 * */
public class NotificationListModel {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<NotificationModel> notificationDetail;

    // Constructor to initialize member variables to its default
    public NotificationListModel() {
        status = null;
        notificationDetail = null;
    }

    public NotificationListModel(String status, List<NotificationModel> notificationDetail) {
        this.status = status;
        this.notificationDetail = notificationDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NotificationModel> getNotificationDetail() {
        return notificationDetail;
    }

    public void setNotificationDetail(List<NotificationModel> notificationDetail) {
        this.notificationDetail = notificationDetail;
    }
}