package com.education.librarymanagement.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.education.librarymanagement.R;
import com.education.librarymanagement.model.UserModel;
import com.education.librarymanagement.view.LoginActivity;
import com.education.librarymanagement.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

/*
 * UI Class to hold Login Fragment
 *
 * */
public class RegisterFragment extends Fragment {

    // View Model Object to interact with Business Logic
    private UserViewModel UserViewModel;

    private Button mSignUp;
    private TextInputEditText mUserMobile, mUserPassword;
    private TextInputEditText mUserName, mUserEmail, mUserAddress;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        UserViewModel = ViewModelProviders.of(RegisterFragment.this).get(UserViewModel.class);
    }

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_registration, container, false);

        initializeViews(rootView);

        return rootView;
    }

    // Android's Callback when Fragment's View is Resumed
    @Override
    public void onResume() {
        super.onResume();
    }

    // Android's Callback when Fragment is to be Destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove Observer for ViewModel
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mUserName = rootView.findViewById(R.id.et_register_name);
        mUserEmail = rootView.findViewById(R.id.et_register_email);
        mUserMobile = rootView.findViewById(R.id.et_register_mobile);
        mUserAddress = rootView.findViewById(R.id.et_register_address);
        mUserPassword = rootView.findViewById(R.id.et_register_password);
        mSignUp = rootView.findViewById(R.id.bt_register_signup);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                String errMsg = validData();
                if (errMsg != null) {
                    Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] nameList = mUserName.getText().toString().split(" ");
                String firstName = nameList.length > 0 ? nameList[0] : "";
                String lastName = nameList.length > 1 ? nameList[1] : "";

                UserModel userModel = new UserModel(firstName, lastName,
                        mUserEmail.getText().toString(), mUserMobile.getText().toString(),
                        mUserPassword.getText().toString(), mUserAddress.getText().toString());

                UserViewModel.registerUser(userModel).observe(RegisterFragment.this, registerModelObserver);
            }
        });
    }

    private Observer<String> registerModelObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            String toastMessage = "User Registration " + message;

            Toast.makeText(getContext(), toastMessage.toUpperCase(), Toast.LENGTH_SHORT).show();

            if (message.equals("success")) {
                Message msg = new Message();
                msg.what = 2;
                ((LoginActivity) getActivity()).postMessage(msg, 1000);
            }
        }
    };

    private String validData() {
        String msg = null;

        String value = mUserName.getText().toString();
        if (TextUtils.isEmpty(value)) {
            msg = "Enter Valid Name";
        }

        value = mUserEmail.getText().toString();
        if (msg == null && TextUtils.isEmpty(value)) {
            msg = "Enter Valid Email";
        }

        value = mUserMobile.getText().toString();
        if (msg == null && TextUtils.isEmpty(value)) {
            msg = "Enter Valid Mobile";
        }

        value = mUserPassword.getText().toString();
        if (msg == null && TextUtils.isEmpty(value)) {
            msg = "Enter Valid Password";
        }

        value = mUserAddress.getText().toString();
        if (msg == null && TextUtils.isEmpty(value)) {
            msg = "Enter Valid Address";
        }

        return msg;
    }
}