package com.education.librarymanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.education.librarymanagement.model.BookListModel;
import com.education.librarymanagement.model.BookModel;
import com.education.librarymanagement.model.NotificationListModel;
import com.education.librarymanagement.model.RentListModel;
import com.education.librarymanagement.model.RequestModel;
import com.education.librarymanagement.model.ResponseModel;
import com.education.librarymanagement.utils.RetrofitClient;
import com.education.librarymanagement.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * ViewModel to Hold Book Details Data.
 * It handles requests from View to perform CRUD operations in Data via
 * interacting between Repo and Model
 *
 * */
public class BooksViewModel extends ViewModel {

    private MutableLiveData<String> mutableAddBook;
    private MutableLiveData<String> mutableIssueBook;
    private MutableLiveData<String> mutableReturnBook;
    private MutableLiveData<String> mutableUpdateBook;
    private MutableLiveData<String> mutableDeleteBook;
    private MutableLiveData<String> mutableReserveBook;
    private MutableLiveData<String> mutableSendNotification;
    private MutableLiveData<RentListModel> mutableRentsList;
    private MutableLiveData<BookListModel> mutableBooksList;
    private MutableLiveData<BookListModel> mutableBookDetails;
    private MutableLiveData<RequestModel> mutableRequestsList;
    private MutableLiveData<NotificationListModel> mutableNotificationsList;

    // Constructor to initialize variables
    public BooksViewModel() {

    }

    /*
     * ADD Book
     *
     * @bookDetails: Book Models
     *
     * @returns BOOK Add Status
     * */
    public LiveData<String> addBook(BookModel bookDetails) {
        if (mutableAddBook == null) {
            mutableAddBook = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .addBook(bookDetails.getName(), bookDetails.getAuthor(),
                        bookDetails.getPublisher(), bookDetails.getIsbn(),
                        bookDetails.getYearOfPublish(), bookDetails.getBookCount(),
                        bookDetails.getDescription(), Utility.getLoggedInUser().getFirstName(),
                        Utility.FORMAT_DATE_TIME.format(System.currentTimeMillis()),
                        Utility.getLoggedInUser().getFirstName(),
                        Utility.FORMAT_DATE_TIME.format(System.currentTimeMillis()))
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableAddBook.setValue("Add Book  " + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableAddBook.setValue("Add Book Fail");
                    }
                });

        return mutableAddBook;
    }

    /*
     * UPDATE Book
     *
     * @bookDetails: Book Models
     *
     * @returns BOOK Update Status
     * */
    public LiveData<String> updateBook(BookModel bookDetails) {
        if (mutableUpdateBook == null) {
            mutableUpdateBook = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .updateBook(bookDetails.getBookId(), bookDetails.getName(), bookDetails.getAuthor(),
                        bookDetails.getPublisher(), bookDetails.getIsbn(), bookDetails.getYearOfPublish(),
                        bookDetails.getBookCount(), bookDetails.getDescription(),
                        Utility.getLoggedInUser().getFirstName(), Utility.FORMAT_DATE_TIME.format(System.currentTimeMillis()),
                        Utility.getLoggedInUser().getFirstName(), Utility.FORMAT_DATE_TIME.format(System.currentTimeMillis()))
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableUpdateBook.setValue("Update Book " + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableUpdateBook.setValue("Update Book Fail");
                    }
                });

        return mutableUpdateBook;
    }

    /*
     * DELETE Book
     *
     * @bookID: Book ID
     *
     * @returns BOOK Delete Status
     * */
    public LiveData<String> deleteBook(int bookId) {
        if (mutableDeleteBook == null) {
            mutableDeleteBook = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .deleteBook(bookId)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableDeleteBook.setValue("Delete Book " + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableDeleteBook.setValue("Delete Book Fail");
                    }
                });

        return mutableDeleteBook;
    }

    /*
     * RESERVE a Book
     *
     * @bookId: Book ID
     * @userId: User ID
     * @startDate: Rent Start Date
     * @endDate: Rent End Date
     * @comments: User Comments
     *
     * @returns BOOK Reserve Status
     * */
    public LiveData<String> reserveBook(int userId, int bookId, String startDate, String endDate, String comments) {
        if (mutableReserveBook == null) {
            mutableReserveBook = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .reserveBook(bookId, userId, startDate, endDate, comments)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableReserveBook.setValue(response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableReserveBook.setValue("fail");
                    }
                });

        return mutableReserveBook;
    }

    /*
     * ISSUE Book to an User
     *
     * @userId : User ID
     * @bookId : Book ID
     * @startDate : Start Date
     * @renewalDate : End Date
     *
     * @returns Issue Book Status
     * */
    public LiveData<String> issueBook(int userId, int bookId, String issueDate, String renewalDate) {
        if (mutableIssueBook == null) {
            mutableIssueBook = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .rentBook(userId, bookId, issueDate, renewalDate)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableIssueBook.setValue(response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableIssueBook.setValue("fail");
                    }
                });

        return mutableIssueBook;
    }

    /*
     * BOOKs List
     *
     * @returns BOOKs List
     * */
    public LiveData<BookListModel> bookList() {
        if (mutableBooksList == null) {
            mutableBooksList = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .bookList()
                .enqueue(new Callback<BookListModel>() {
                    @Override
                    public void onResponse(Call<BookListModel> call, Response<BookListModel> response) {
                        if (response.isSuccessful()) {
                            mutableBooksList.setValue(new BookListModel(response.body().getStatus(),
                                    response.body().getBookDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<BookListModel> call, Throwable t) {
                        mutableBooksList.setValue(new BookListModel("fail", null));
                    }
                });

        return mutableBooksList;
    }

    /*
     * BOOK Details
     *
     * @bookId: Book ID
     *
     * @returns BOOK Details
     * */
    public LiveData<BookListModel> getBook(int bookId) {
        if (mutableBookDetails == null) {
            mutableBookDetails = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .getBook(bookId)
                .enqueue(new Callback<BookListModel>() {
                    @Override
                    public void onResponse(Call<BookListModel> call, Response<BookListModel> response) {
                        if (response.isSuccessful()) {
                            mutableBookDetails.setValue(new BookListModel(response.body().getStatus(),
                                    response.body().getBookDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<BookListModel> call, Throwable t) {
                        mutableBookDetails.setValue(new BookListModel("fail", null));
                    }
                });

        return mutableBookDetails;
    }

    /*
     * REQUEST List
     *
     * @userId: User ID
     *
     * @returns REQUESTs List
     * */
    public LiveData<RequestModel> requestList(int userId) {
        if (mutableRequestsList == null) {
            mutableRequestsList = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .requestList(userId)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            //mutableRequestsList.setValue(response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        //mutableRequestsList.setValue("fail");
                    }
                });

        return mutableRequestsList;
    }

    /*
     * RENTED Books List
     *
     * @returns RENTs List
     * */
    public LiveData<RentListModel> rentList() {
        if (mutableRentsList == null) {
            mutableRentsList = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .rentList()
                .enqueue(new Callback<RentListModel>() {
                    @Override
                    public void onResponse(Call<RentListModel> call, Response<RentListModel> response) {
                        if (response.isSuccessful()) {
                            mutableRentsList.setValue(new RentListModel(response.body().getStatus(),
                                    response.body().getRentDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RentListModel> call, Throwable t) {
                        mutableRentsList.setValue(new RentListModel("Fail", null));
                    }
                });

        return mutableRentsList;
    }

    /*
     * RENTED Books List for a User
     *
     * @userId: User
     *
     * @returns RENTs List
     * */
    public LiveData<RentListModel> getRentedBooks(int userId) {
        if (mutableRentsList == null) {
            mutableRentsList = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .getRentedBooks(userId)
                .enqueue(new Callback<RentListModel>() {
                    @Override
                    public void onResponse(Call<RentListModel> call, Response<RentListModel> response) {
                        if (response.isSuccessful()) {
                            mutableRentsList.setValue(new RentListModel(response.body().getStatus(),
                                    response.body().getRentDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RentListModel> call, Throwable t) {
                        mutableRentsList.setValue(new RentListModel("Fail", null));
                    }
                });

        return mutableRentsList;
    }

    /*
     * RETURN Book to Library
     *
     * @rentId : Rent ID
     * @returnDate : Return Date ( Current Date )
     *
     * @returns Return Book Status
     * */
    public LiveData<String> returnBook(int rentId, String returnDate) {
        if (mutableReturnBook == null) {
            mutableReturnBook = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .returnBook(rentId, returnDate)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableReturnBook.setValue(response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableReturnBook.setValue("fail");
                    }
                });

        return mutableReturnBook;
    }

    /*
     * Notifications List for a User
     *
     * @userId: User
     *
     * @returns Notifications List
     * */
    public LiveData<NotificationListModel> getNotifications(int userId) {
        if (mutableNotificationsList == null) {
            mutableNotificationsList = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .getNotifications(userId)
                .enqueue(new Callback<NotificationListModel>() {
                    @Override
                    public void onResponse(Call<NotificationListModel> call, Response<NotificationListModel> response) {
                        if (response.isSuccessful()) {
                            mutableNotificationsList.setValue(new NotificationListModel(response.body().getStatus(),
                                    response.body().getNotificationDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationListModel> call, Throwable t) {
                        mutableNotificationsList.setValue(new NotificationListModel("Fail", null));
                    }
                });

        return mutableNotificationsList;
    }

    /*
     * Send Notifications to Users
     *
     * @userId : User ID
     * @message : Notification Message
     *
     * @returns Notification Sent Status
     * */
    public LiveData<String> sendNotification(int userId, String message) {
        if (mutableSendNotification == null) {
            mutableSendNotification = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .sendNotification(userId, message, System.currentTimeMillis() + "")
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableSendNotification.setValue(response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableSendNotification.setValue("fail");
                    }
                });

        return mutableSendNotification;
    }
}