package com.education.librarymanagement.model;

public class ResponseModel {
    String status;

    public ResponseModel(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
