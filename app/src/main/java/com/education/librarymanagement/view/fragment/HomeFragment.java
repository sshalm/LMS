package com.education.librarymanagement.view.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.education.librarymanagement.R;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;

/*
 * UI Class to hold Login Fragment
 *
 * */
public class HomeFragment extends Fragment {

    CardView mAddBook, mIssueBook;
    CardView mNotifications, mLogout;
    CardView mViewBooks, mRentedBooks, mRequests;

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int layoutID = R.layout.fragment_user_main;
        if (Utility.isUserADMIN()) {
            layoutID = R.layout.fragment_admin_main;
        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(layoutID, container, false);

        if (Utility.isUserADMIN()) {
            initializeAdminViews(rootView);
        } else {
            initializeUserViews(rootView);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Hello " + Utility.getLoggedInUser().getFirstName().toUpperCase());
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeAdminViews(View rootView) {
        mAddBook = rootView.findViewById(R.id.card_admin_add_book);
        mAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_ADD_BOOK;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mIssueBook = rootView.findViewById(R.id.card_admin_issue_book);
        mIssueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_ISSUE_BOOK;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mViewBooks = rootView.findViewById(R.id.card_admin_view_books);
        mViewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_VIEW_BOOKS;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mRequests = rootView.findViewById(R.id.card_admin_book_requests);

        mRentedBooks = rootView.findViewById(R.id.card_admin_rented_books);
        mRentedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_RENTED_BOOKS;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mNotifications = rootView.findViewById(R.id.card_admin_notifications);
        mNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_NOTIFICATIONS;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mLogout = rootView.findViewById(R.id.card_admin_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.saveUserDetails(getContext(), null);
                Utility.setLoggedInUser(null);

                Message msg = new Message();
                msg.what = 1;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });
    }

    private void initializeUserViews(View rootView) {
        mViewBooks = rootView.findViewById(R.id.card_user_view_books);
        mRequests = rootView.findViewById(R.id.card_user_book_requests);
        mRentedBooks = rootView.findViewById(R.id.card_user_rented_books);
        mNotifications = rootView.findViewById(R.id.card_user_notifications);
        mLogout = rootView.findViewById(R.id.card_user_logout);

        mViewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_VIEW_BOOKS;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mRentedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = MainActivity.FRAG_RENTED_BOOKS;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.saveUserDetails(getContext(), null);
                Utility.setLoggedInUser(null);

                Message msg = new Message();
                msg.what = 1;
                ((MainActivity) getActivity()).postMessage(msg, 0);
            }
        });
    }
}