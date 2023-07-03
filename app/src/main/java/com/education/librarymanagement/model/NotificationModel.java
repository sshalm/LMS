package com.education.librarymanagement.model;

/*
 * Model Class to hold Notification Details
 * */
public class NotificationModel {
    int userId;
    long timeStamp;
    String description;
    String notificationDate;

    public NotificationModel(int userId, long timeStamp, String description, String notificationDate) {
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.description = description;
        this.notificationDate = notificationDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}