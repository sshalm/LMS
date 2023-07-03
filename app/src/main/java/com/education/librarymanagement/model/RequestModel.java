package com.education.librarymanagement.model;

/*
 * Model Class to hold Request Details
 * */
public class RequestModel {
    int bookId;
    int userId;
    String startDate;
    String endDate;
    String name;
    String author;
    String publisher;
    String isbn;
    String yearOfPublish;
    String description;

    public RequestModel(String name, String author, String publisher, String isbn, String yearOfPublish,
                        String description) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.yearOfPublish = yearOfPublish;
        this.description = description;
    }

    public RequestModel(int bookId, String name, String author, String publisher, String isbn,
                        String yearOfPublish, String description) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.yearOfPublish = yearOfPublish;
        this.description = description;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getYearOfPublish() {
        return yearOfPublish;
    }

    public void setYearOfPublish(String yearOfPublish) {
        this.yearOfPublish = yearOfPublish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}