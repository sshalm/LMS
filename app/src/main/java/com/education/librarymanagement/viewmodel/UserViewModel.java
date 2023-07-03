package com.education.librarymanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.education.librarymanagement.model.LoginModel;
import com.education.librarymanagement.model.ResponseModel;
import com.education.librarymanagement.model.UserListModel;
import com.education.librarymanagement.model.UserModel;
import com.education.librarymanagement.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * ViewModel to Hold User Login Details Data.
 * It handles requests from View to perform CRUD operations in Data via
 * interacting between Repo and Model
 *
 * */
public class UserViewModel extends ViewModel {

    private MutableLiveData<String> mutableRegisterDetail;
    private MutableLiveData<LoginModel> mutableLoginDetail;
    private MutableLiveData<UserListModel> mutableUsersList;

    // Constructor to initialize variables
    public UserViewModel() {

    }

    /*
     * LOGIN User
     *
     * @email: User Email
     * @password: User Password
     *
     * @returns LOGIN Status
     * */
    public LiveData<LoginModel> loginUser(String email, String password) {
        if (mutableLoginDetail == null) {
            mutableLoginDetail = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .loginUser(email, password)
                .enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if (response.isSuccessful()) {
                            mutableLoginDetail.setValue(new LoginModel(response.body().getStatus(),
                                    response.body().getUserDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        mutableLoginDetail.setValue(new LoginModel("fail", null));
                    }
                });

        return mutableLoginDetail;
    }

    /*
     * REGISTER User
     *
     * @name: User Name
     * @email: User Email
     * @mobile: User Mobile
     * @password: User Password
     * @gender: User Gender
     *
     * @returns REGISTRATION Status
     * */
    public LiveData<String> registerUser(UserModel userModel) {
        if (mutableRegisterDetail == null) {
            mutableRegisterDetail = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .registerUser(userModel.getFirstName(), userModel.getLastName(),
                        userModel.getEmail(), userModel.getPhone(), userModel.getPassword(),
                        userModel.getCurrentAddress(), userModel.getRoleId())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            mutableRegisterDetail.setValue(response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        mutableRegisterDetail.setValue("fail");
                    }
                });

        return mutableRegisterDetail;
    }

    /*
     * USERs List
     *
     * @returns USERs List
     * */
    public LiveData<UserListModel> userList() {
        if (mutableUsersList == null) {
            mutableUsersList = new MutableLiveData<>();
        }

        RetrofitClient.getInstance().getAPIObj()
                .userList()
                .enqueue(new Callback<UserListModel>() {
                    @Override
                    public void onResponse(Call<UserListModel> call, Response<UserListModel> response) {
                        if (response.isSuccessful()) {
                            mutableUsersList.setValue(new UserListModel(response.body().getStatus(),
                                    response.body().getUserDetail()));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserListModel> call, Throwable t) {
                        mutableUsersList.setValue(new UserListModel("fail", null));
                    }
                });

        return mutableUsersList;
    }
}