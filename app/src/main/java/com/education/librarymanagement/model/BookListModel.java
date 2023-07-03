package com.education.librarymanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Model Class to hold Book List
 * */
public class BookListModel {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<BookModel> bookDetail;

    // Constructor to initialize member variables to its default
    public BookListModel() {
        status = null;
        bookDetail = null;
    }

    public BookListModel(String status, List<BookModel> bookDetail) {
        this.status = status;
        this.bookDetail = bookDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BookModel> getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(List<BookModel> bookDetail) {
        this.bookDetail = bookDetail;
    }
}