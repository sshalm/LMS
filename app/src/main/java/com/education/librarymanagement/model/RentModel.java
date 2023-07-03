package com.education.librarymanagement.model;

/*
 * Model Class to hold Rent Details
 * */
public class RentModel {
    int rentId;
    int bookId;
    int userId;
    String issueDate;
    String renewalDate;
    String name;
    String author;
    String publisher;
    String isbn;
    String yearOfPublish;
    String description;
    String firstName;
    String lastName;

    public RentModel(int rentId, int bookId, int userId, String issueDate, String renewalDate,
                     String name, String author, String publisher, String isbn, String yearOfPublish,
                     String description, String firstName, String lastName) {
        this.rentId = rentId;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.renewalDate = renewalDate;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.yearOfPublish = yearOfPublish;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getRentId() {
        return rentId;
    }

    public void setRentId(int rentId) {
        this.rentId = rentId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(String renewalDate) {
        this.renewalDate = renewalDate;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}