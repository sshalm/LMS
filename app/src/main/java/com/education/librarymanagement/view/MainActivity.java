package com.education.librarymanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.education.librarymanagement.R;
import com.education.librarymanagement.view.fragment.AddBookFragment;
import com.education.librarymanagement.view.fragment.BookDetailsFragment;
import com.education.librarymanagement.view.fragment.HomeFragment;
import com.education.librarymanagement.view.fragment.IssueBookFragment;
import com.education.librarymanagement.view.fragment.LoginFragment;
import com.education.librarymanagement.view.fragment.NotificationsFragment;
import com.education.librarymanagement.view.fragment.RentRequestDetailsFragment;
import com.education.librarymanagement.view.fragment.RentedBooksFragment;
import com.education.librarymanagement.view.fragment.RequestedBooksFragment;
import com.education.librarymanagement.view.fragment.ViewBooksFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements LoginFragment.SwitchFragment {

    // Fragment Index Constants
    public static final int FRAG_MAIN = 0;
    public static final int FRAG_VIEW_BOOKS = 1;
    public static final int FRAG_RENTED_BOOKS = 2;
    public static final int FRAG_BOOK_REQUESTS = 3;
    public static final int FRAG_NOTIFICATIONS = 4;
    public static final int FRAG_ISSUE_BOOK = 5;
    public static final int FRAG_ADD_BOOK = 6;
    public static final int FRAG_BOOK_DETAIL = 7;
    public static final int FRAG_RENT_REQUEST_DETAIL = 8;

    // Fragment TAG Constant
    private final String TAG_MAIN = "main";
    private final String TAG_VIEW_BOOKS = "viewbooks";
    private final String TAG_RENTED_BOOKS = "rented";
    private final String TAG_REQUESTS = "requests";
    private final String TAG_NOTIFICATIONS = "notifications";
    private final String TAG_ISSUE_BOOK = "issuebook";
    private final String TAG_ADD_BOOK = "addbook";
    private final String TAG_BOOK_DETAIL = "bookdetail";
    private final String TAG_BOOK_RENT_REQUEST = "rentrequestdetail";

    // Variables to Hold Current Fragment details
    private String currentTag;
    private int currentFragmentIndex;
    private Fragment currentFragmentObj;

    private MainHandler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new MainHandler();

        // Default current Fragment to LoginFragment always
        currentFragmentIndex = FRAG_MAIN;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                if (currentFragmentObj instanceof ViewBooksFragment) {
                    ((ViewBooksFragment) currentFragmentObj).updateScannerResult(intentResult.getContents());
                }
            }
        }
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
                    fragmentTransaction.replace(R.id.mainFrame, currentFragmentObj, currentTag);
                    fragmentTransaction.commitAllowingStateLoss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Post message for MainThread Looper
        if (mPendingRunnable != null) {
            mainHandler.post(mPendingRunnable);
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
            case FRAG_VIEW_BOOKS:
                currentTag = TAG_VIEW_BOOKS;

                return new ViewBooksFragment();

            case FRAG_BOOK_REQUESTS:
                currentTag = TAG_REQUESTS;

                return new RequestedBooksFragment();

            case FRAG_RENTED_BOOKS:
                currentTag = TAG_RENTED_BOOKS;

                return new RentedBooksFragment();

            case FRAG_NOTIFICATIONS:
                currentTag = TAG_NOTIFICATIONS;

                return new NotificationsFragment();

            case FRAG_ISSUE_BOOK:
                currentTag = TAG_ISSUE_BOOK;
                return new IssueBookFragment();

            case FRAG_ADD_BOOK:
                currentTag = TAG_ADD_BOOK;

                return new AddBookFragment();

            case FRAG_BOOK_DETAIL:
                currentTag = TAG_BOOK_DETAIL;

                BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
                bookDetailsFragment.setArguments(bundle);

                return bookDetailsFragment;

            case FRAG_RENT_REQUEST_DETAIL:
                currentTag = TAG_BOOK_RENT_REQUEST;

                RentRequestDetailsFragment rentRequestDetailsFragment = new RentRequestDetailsFragment();
                rentRequestDetailsFragment.setArguments(bundle);

                return rentRequestDetailsFragment;

            default:
            case FRAG_MAIN:
                currentTag = TAG_MAIN;

                return new HomeFragment();
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
        if (mainHandler != null) {
            mainHandler.sendMessageDelayed(msg, delay);
        }
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /*
     * Handler class
     * */
    public class MainHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startLoginActivity();
                    break;

                case 2:
                    closeFragment();
                    break;

                case 3:
                    openFragment(msg.arg1, null);
                    break;
            }
        }
    }

    /*
     * Interface that handle clicks on List's Items
     * */
    public interface ItemClick {
        void itemClicked(int position, int rentId);
    }
}