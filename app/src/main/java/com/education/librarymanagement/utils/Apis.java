package com.education.librarymanagement.utils;

import com.education.librarymanagement.model.BookListModel;
import com.education.librarymanagement.model.LoginModel;
import com.education.librarymanagement.model.NotificationListModel;
import com.education.librarymanagement.model.RentListModel;
import com.education.librarymanagement.model.ResponseModel;
import com.education.librarymanagement.model.UserListModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Apis {

    String BASE_URL = "http://coolclouds.org/lms/api/";

    @POST("user")
    @FormUrlEncoded
    Call<LoginModel> loginUser(@Field("email") String email, @Field("password") String password);

    @POST("register")
    @FormUrlEncoded
    Call<ResponseModel> registerUser(@Field("firstName") String firstName, @Field("lastName") String lastName,
                                     @Field("email") String email, @Field("phone") String phone,
                                     @Field("password") String password, @Field("currentAddress") String currentAddress,
                                     @Field("roleId") int roleId);

    @POST("addBook")
    @FormUrlEncoded
    Call<ResponseModel> addBook(@Field("name") String name, @Field("author") String author,
                                @Field("publisher") String publisher, @Field("isbn") String isbn,
                                @Field("yearofpublish") String yearOfPublish, @Field("count") String count,
                                @Field("description") String description, @Field("createdBy") String createdBy,
                                @Field("createdAt") String createdAt, @Field("updatedBy") String updatedBy,
                                @Field("updatedAt") String updatedAt);

    @POST("editBook")
    @FormUrlEncoded
    Call<ResponseModel> updateBook(@Field("bookid") int bookId, @Field("name") String name,
                                   @Field("author") String author, @Field("publisher") String publisher,
                                   @Field("isbn") String isbn, @Field("yearofpublish") String yearOfPublish,
                                   @Field("count") String count, @Field("description") String description,
                                   @Field("createdBy") String createdBy, @Field("createdAt") String createdAt,
                                   @Field("updatedBy") String updatedBy, @Field("updatedAt") String updatedAt);

    @POST("reservebook")
    @FormUrlEncoded
    Call<ResponseModel> reserveBook(@Field("bookid") int bookId, @Field("userid") int userId,
                                    @Field("startdate") String startDate, @Field("enddate") String endDate,
                                    @Field("comments") String comments);

    @POST("issueBook")
    @FormUrlEncoded
    Call<ResponseModel> rentBook(@Field("userid") int userid, @Field("bookid") int bookid,
                                 @Field("issuedate") String issuedate, @Field("renewaldate") String renewaldate);

    @POST("returnBook")
    @FormUrlEncoded
    Call<ResponseModel> returnBook(@Field("rentid") int rentid, @Field("returndate") String returndate);

    @POST("requestlist")
    @FormUrlEncoded
    Call<ResponseModel> requestList(@Field("userid") int userId);

    @GET("getrentedbooks")
    Call<RentListModel> rentList();

    @GET("getrentedbooks/{userid}")
    Call<RentListModel> getRentedBooks(@Path(value = "userid") int userid);

    @GET("getnotifications/{userid}")
    Call<NotificationListModel> getNotifications(@Path(value = "userid") int userid);

    @POST("notification")
    @FormUrlEncoded
    Call<ResponseModel> sendNotification(@Field("userid") int userid, @Field("description") String description,
                                         @Field("timestamp") String timestamp);

    @GET("books")
    Call<BookListModel> bookList();

    @GET("users")
    Call<UserListModel> userList();

    @GET("books/{bookid}")
    Call<BookListModel> getBook(@Path(value = "bookid") int bookId);

    @DELETE("books/{bookid}")
    Call<ResponseModel> deleteBook(@Path(value = "bookid") int bookId);

    @POST("response")
    @FormUrlEncoded
    Call<ResponseModel> response(@Field("requestid") int requestId,
                                 @Field("roleid") int roleId, @Field("action") int action);
}