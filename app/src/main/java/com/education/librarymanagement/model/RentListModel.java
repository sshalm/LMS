package com.education.librarymanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Model Class to hold Rent List
 * */
public class RentListModel {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<RentModel> rentDetail;

    // Constructor to initialize member variables to its default
    public RentListModel() {
        status = null;
        rentDetail = null;
    }

    public RentListModel(String status, List<RentModel> rentDetail) {
        this.status = status;
        this.rentDetail = rentDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RentModel> getRentDetail() {
        return rentDetail;
    }

    public void setRentDetail(List<RentModel> rentDetail) {
        this.rentDetail = rentDetail;
    }
}