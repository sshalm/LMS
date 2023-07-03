package com.education.librarymanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.education.librarymanagement.R;
import com.education.librarymanagement.view.fragment.LoginFragment;
import com.education.librarymanagement.view.fragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.SwitchFragment {

    // Fragment Index Constants
    public static final int FRAG_LOGIN = 0;
    public static final int FRAG_REGISTER = 1;

    // Fragment TAG Constant
    private final String TAG_LOGIN = "login";
    private final String TAG_REGISTER = "register";

    // Variables to Hold Current Fragment details
    private String currentTag;
    private int currentFragmentIndex;
    private Fragment currentFragmentObj;

    private LoginHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHandler = new LoginHandler();

        // Default current Fragment to LoginFragment always
        currentFragmentIndex = FRAG_LOGIN;

        loadFragment(null);
    }

    // Handle System Back Key Press Event
    @Override
    public void onBackPressed() {
        // If HomeFragment is currently visible, close the Activity (Application)
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }

        super.onBackPressed();
    }

    /*
     * Load a Fragment in this Activity
     * @bundle: Any data that has to be passed on to this Fragment
     *
     * */
    private void loadFragment(final Bundle bundle) {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.addToBackStack(currentTag);

                    currentFragmentObj = getFragment(bundle);

                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.loginFrame, currentFragmentObj, currentTag);
                    fragmentTransaction.commitAllowingStateLoss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Post message for MainThread Looper
        if (mPendingRunnable != null) {
            loginHandler.post(mPendingRunnable);
        }
    }

    /*
     * Get Fragment to display's Object
     *
     * @bundle: Data to be passed to this New Fragment
     *
     * */
    private Fragment getFragment(Bundle bundle) {
        switch (currentFragmentIndex) {
            case FRAG_REGISTER:
                currentTag = TAG_REGISTER;

                RegisterFragment registerFragment = new RegisterFragment();
                registerFragment.setArguments(bundle);
                return registerFragment;

            default:
            case FRAG_LOGIN:
                currentTag = TAG_LOGIN;

                return new LoginFragment();
        }
    }

    // Interface Function to handle request to Open a Particular Fragment
    @Override
    public void openFragment(int fragmentIndex, Bundle data) {
        currentFragmentIndex = fragmentIndex;

        loadFragment(data);
    }

    // Interface Function to handle request to Close a Particular Fragment
    @Override
    public void closeFragment() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void postMessage(Message msg, long delay) {
        if (loginHandler != null) {
            loginHandler.sendMessageDelayed(msg, delay);
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /*
     * Handler class
     * */
    public class LoginHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startMainActivity();
                    break;

                case 2:
                    closeFragment();
                    break;
            }
        }
    }
}