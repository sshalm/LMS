package com.education.librarymanagement.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
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
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;
import com.education.librarymanagement.viewmodel.BooksViewModel;
import com.google.android.material.textfield.TextInputEditText;

/*
 * UI Class to hold Rent/Request Details Fragment
 *
 * */
public class RentRequestDetailsFragment extends Fragment {

    // View Model Object to interact with Business Logic
    private BooksViewModel booksViewModel;

    private Button mAccept, mReject, mCancel;
    private TextView mBookName, mBookStatus, mDueDate;
    private LinearLayout mUserView, mAdminView, mBookDueView;
    private TextInputEditText mUserName, mRentDate, mRentDuration;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        booksViewModel = ViewModelProviders.of(RentRequestDetailsFragment.this).get(BooksViewModel.class);
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
                R.layout.fragment_rent_request_detail, container, false);

        if (Utility.isUserADMIN()) {
            initializeAdminViews(rootView);
        } else {
            initializeUserViews(rootView);
        }

        return rootView;
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeAdminViews(View rootView) {
        mUserName = rootView.findViewById(R.id.et_rentrequest_username);
        mRentDate = rootView.findViewById(R.id.et_rentrequest_startdate);
        mRentDuration = rootView.findViewById(R.id.et_rentrequest_duration);

        mUserView = rootView.findViewById(R.id.ll_rentrequest_user);
        mAdminView = rootView.findViewById(R.id.ll_rentrequest_admin);
        mBookDueView = rootView.findViewById(R.id.ll_rentrequest_rentstatus);

        mBookName = rootView.findViewById(R.id.tv_rentrequest_name);
        mBookStatus = rootView.findViewById(R.id.tv_rentrequest_status);
        mDueDate = rootView.findViewById(R.id.tv_rentrequest_duedate);

        mAccept = rootView.findViewById(R.id.bt_rentrequest_accept);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                /*BookModel bookModel = new BookModel(mBookName.getText().toString(),
                        mAuthor.getText().toString(), mPublisher.getText().toString(),
                        mISBN.getText().toString(), mYear.getText().toString(), mDescription.getText().toString());

                booksViewModel.updateBook(bookModel).observe(RentRequestDetailsFragment.this, rentRequestObserver);*/
            }
        });

        mReject = rootView.findViewById(R.id.bt_rentrequest_reject);
        mReject.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                //booksViewModel.deleteBook(1).observe(RentRequestDetailsFragment.this, modifyBookObserver);
            }
        });

        enableViews(true);
    }

    private void initializeUserViews(View rootView) {
        mUserName = rootView.findViewById(R.id.et_rentrequest_username);
        mRentDate = rootView.findViewById(R.id.et_rentrequest_startdate);
        mRentDuration = rootView.findViewById(R.id.et_rentrequest_duration);

        mUserView = rootView.findViewById(R.id.ll_rentrequest_user);
        mAdminView = rootView.findViewById(R.id.ll_rentrequest_admin);
        mBookDueView = rootView.findViewById(R.id.ll_rentrequest_rentstatus);

        mBookName = rootView.findViewById(R.id.tv_rentrequest_name);
        mBookStatus = rootView.findViewById(R.id.tv_rentrequest_status);
        mDueDate = rootView.findViewById(R.id.tv_rentrequest_duedate);

        mCancel = rootView.findViewById(R.id.bt_rentrequest_accept);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                /*BookModel bookModel = new BookModel(mBookName.getText().toString(),
                        mAuthor.getText().toString(), mPublisher.getText().toString(),
                        mISBN.getText().toString(), mYear.getText().toString(), mDescription.getText().toString());

                booksViewModel.updateBook(bookModel).observe(RentRequestDetailsFragment.this, rentRequestObserver);*/
            }
        });

        enableViews(false);
    }

    private void enableViews(boolean isAdmin) {
        mUserView.setVisibility(isAdmin ? View.GONE : View.VISIBLE);
        mAdminView.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
    }

    private Observer<String> rentRequestObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(getContext(), message.toUpperCase(), Toast.LENGTH_SHORT).show();

            if (message.contains("success")) {
                Message msg = new Message();
                msg.what = 2;
                ((MainActivity) getActivity()).postMessage(msg, 1000);
            }
        }
    };
}