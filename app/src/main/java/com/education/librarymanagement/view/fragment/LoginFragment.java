package com.education.librarymanagement.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.education.librarymanagement.R;
import com.education.librarymanagement.model.LoginModel;
import com.education.librarymanagement.model.UserModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.LoginActivity;
import com.education.librarymanagement.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

/*
 * UI Class to hold Login Fragment
 *
 * */
public class LoginFragment extends Fragment {

    // View Model Object to interact with Business Logic
    private UserViewModel UserViewModel;

    private Button mLogin;
    private TextView mSignUpLink, mWelcomeMsg;
    private LinearLayout mSignInView, mLoginView;
    private TextInputEditText mUserEmail, mUserPassword;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        UserViewModel = ViewModelProviders.of(LoginFragment.this).get(UserViewModel.class);
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
                R.layout.fragment_login, container, false);

        initializeViews(rootView);
        initData();

        return rootView;
    }

    private void initData() {
        UserModel userDetails = Utility.getUserDetails(getContext());
        if (userDetails != null && userDetails.getUserId() > 0) {
            Utility.setLoggedInUser(userDetails);

            mLoginView.setVisibility(View.GONE);
            mSignInView.setVisibility(View.VISIBLE);
            mWelcomeMsg.setText("Welcome " + userDetails.getFirstName() + " " + userDetails.getLastName());

            // Move to Home Screen
            Message msg = new Message();
            msg.what = 1;
            ((LoginActivity) getActivity()).postMessage(msg, 2000);
        }
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mLoginView = rootView.findViewById(R.id.view_login);
        mSignInView = rootView.findViewById(R.id.view_signin);
        mLogin = rootView.findViewById(R.id.bt_login_signin);
        mSignUpLink = rootView.findViewById(R.id.tv_login_signup);
        mWelcomeMsg = rootView.findViewById(R.id.tv_login_welcome);
        mUserEmail = rootView.findViewById(R.id.et_login_useremail);
        mUserPassword = rootView.findViewById(R.id.et_login_password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                String message = validData();
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    return;
                }

                UserViewModel.loginUser(mUserEmail.getText().toString(), mUserPassword.getText().toString())
                        .observe(LoginFragment.this, loginModelObserver);
            }
        });

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).openFragment(LoginActivity.FRAG_REGISTER, null);
            }
        });
    }

    private Observer<LoginModel> loginModelObserver = new Observer<LoginModel>() {
        @Override
        public void onChanged(LoginModel loginModel) {
            if (loginModel.getStatus().equals("fail")) {
                Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }

            if (loginModel.getStatus().equals("success")) {
                List<UserModel> userDetails = loginModel.getUserDetail();

                if (userDetails != null && userDetails.size() > 0) {
                    mSignInView.setVisibility(View.VISIBLE);
                    mLoginView.setVisibility(View.GONE);

                    mWelcomeMsg.setText("Welcome " + userDetails.get(0).getFirstName()
                            + " " + userDetails.get(0).getLastName());

                    // Store User Details and Save in SharedPrefs
                    Utility.setLoggedInUser(userDetails.get(0));
                    Utility.saveUserDetails(getContext(), userDetails.get(0));

                    // Move to Home Screen
                    Message msg = new Message();
                    msg.what = 1;
                    ((LoginActivity) getActivity()).postMessage(msg, 2000);
                }
            }
        }
    };

    private String validData() {
        String errMsg = null;

        String value = mUserEmail.getText().toString();
        if (TextUtils.isEmpty(value)) {
            errMsg = "Enter Valid EMail ID";
        }
        value = mUserPassword.getText().toString();
        if (errMsg == null && TextUtils.isEmpty(value)) {
            errMsg = "Enter Valid Password";
        }

        return errMsg;
    }

    /*
     * Interface to provide a way for Activity to communicate with available Fragments
     *
     * */
    public interface SwitchFragment {
        /* Open Fragment Request
         * @fragmentIndex: Index No of Fragment to Open. Defined in @MainActivity
         * @data: Any Data to be passed as an argument to this Fragment
         */
        void openFragment(int fragmentIndex, Bundle data);

        // Close Fragment Request
        void closeFragment();
    }
}