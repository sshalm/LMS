package com.education.librarymanagement.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.education.librarymanagement.R;
import com.education.librarymanagement.model.BookListModel;
import com.education.librarymanagement.model.BookModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;
import com.education.librarymanagement.viewmodel.BooksViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

/*
 * UI Class to hold Login Fragment
 *
 * */
public class BookDetailsFragment extends Fragment {

    private int mDatePickerType;
    private int mEndYear, mEndMonth, mEndDay;
    private int mStartYear, mStartMonth, mStartDay;

    private long mStartDateMillis, mEndDateMillis;

    // View Model Object to interact with Business Logic
    private BooksViewModel booksViewModel;
    private BookModel mBookDetails;

    private TextView mBookStatus;
    private Button mStartDate, mEndDate;
    private LinearLayout mAdminView, mUserView;
    private Button mUpdateBook, mDeleteBook, mRequestBook;
    private TextInputEditText mBookName, mAuthor, mISBN, mComments;
    private TextInputEditText mPublisher, mYear, mCount, mDescription;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        booksViewModel = ViewModelProviders.of(BookDetailsFragment.this).get(BooksViewModel.class);
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
                R.layout.fragment_book_detail, container, false);

        initializeViews(rootView);

        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Book Details");
    }

    private void initializeViews(View rootView) {
        mAdminView = rootView.findViewById(R.id.ll_bookdetail_modify);
        mUserView = rootView.findViewById(R.id.ll_bookdetail_requests);
        mBookName = rootView.findViewById(R.id.et_bookdetail_name);
        mAuthor = rootView.findViewById(R.id.et_bookdetail_author);
        mPublisher = rootView.findViewById(R.id.et_bookdetail_publisher);
        mYear = rootView.findViewById(R.id.et_bookdetail_year);
        mISBN = rootView.findViewById(R.id.et_bookdetail_isbn);
        mCount = rootView.findViewById(R.id.et_bookdetail_count);
        mDescription = rootView.findViewById(R.id.et_bookdetail_description);
        mBookStatus = rootView.findViewById(R.id.tv_bookdetail_bookstatus);

        if (Utility.isUserADMIN()) {
            initializeAdminViews(rootView);
        } else {
            initializeUserViews(rootView);
        }
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeAdminViews(View rootView) {
        mAdminView.setVisibility(View.VISIBLE);
        mUserView.setVisibility(View.GONE);

        mUpdateBook = rootView.findViewById(R.id.bt_bookdetail_update);
        mUpdateBook.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                String errMsg = validData();
                if (errMsg != null) {
                    Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog confirmDialog = new AlertDialog.Builder(getContext())
                        .setTitle("UPDATE BOOK ??")
                        .setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BookModel bookModel = new BookModel(mBookDetails.getBookId(),
                                        mBookName.getText().toString(), mAuthor.getText().toString(),
                                        mPublisher.getText().toString(), mISBN.getText().toString(),
                                        mYear.getText().toString(), mCount.getText().toString(),
                                        mDescription.getText().toString());

                                booksViewModel.updateBook(bookModel).observe(getViewLifecycleOwner(), modifyBookObserver);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();

                confirmDialog.show();
            }
        });

        mDeleteBook = rootView.findViewById(R.id.bt_bookdetail_delete);
        mDeleteBook.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                AlertDialog confirmDialog = new AlertDialog.Builder(getContext())
                        .setTitle("DELETE BOOK ??")
                        .setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                booksViewModel.deleteBook(mBookDetails.getBookId())
                                        .observe(getViewLifecycleOwner(), modifyBookObserver);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();

                confirmDialog.show();
            }
        });

        enableViews(true);
    }

    private void initData() {
        if (getArguments() != null) {
            booksViewModel.getBook(getArguments().getInt("book_id")).observe(getViewLifecycleOwner(), bookDetailObserver);
        }

        if (!Utility.isUserADMIN()) {
            // Initialize Dates to Today
            Calendar calendar = Calendar.getInstance();
            mStartYear = calendar.get(Calendar.YEAR);
            mStartMonth = calendar.get(Calendar.MONTH);
            mStartDay = calendar.get(Calendar.DAY_OF_MONTH);
            mStartDateMillis = calendar.getTimeInMillis();

            String dateString = Utility.FORMAT_DATE.format(mStartDateMillis);
            mStartDate.setText(dateString);

            mEndYear = calendar.get(Calendar.YEAR);
            mEndMonth = calendar.get(Calendar.MONTH);
            mEndDay = calendar.get(Calendar.DAY_OF_MONTH);
            mEndDateMillis = mStartDateMillis;
        }
    }

    private void initializeUserViews(View rootView) {
        mAdminView.setVisibility(View.GONE);
        mUserView.setVisibility(View.VISIBLE);

        mBookName.setTextColor(Color.BLACK);
        mAuthor.setTextColor(Color.BLACK);
        mPublisher.setTextColor(Color.BLACK);
        mYear.setTextColor(Color.BLACK);
        mISBN.setTextColor(Color.BLACK);
        mCount.setTextColor(Color.BLACK);
        mDescription.setTextColor(Color.BLACK);

        mEndDate = rootView.findViewById(R.id.bt_bookdetail_enddate);
        mComments = rootView.findViewById(R.id.et_bookdetail_comments);
        mStartDate = rootView.findViewById(R.id.bt_bookdetail_startdate);
        mRequestBook = rootView.findViewById(R.id.bt_bookdetail_request);

        mComments.setEnabled(true);

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickerType = 1;

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, mStartYear, mStartMonth, mStartDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Don't allow Past Dates
                datePickerDialog.show();
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickerType = 2;

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, mEndYear, mEndMonth, mEndDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Don't allow Past Dates
                datePickerDialog.show();
            }
        });

        mRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String errMsg = validData();
                if (errMsg != null) {
                    Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                //booksViewModel.requestBook().observe(getViewLifecycleOwner(), observer);
            }
        });

        enableViews(false);
    }

    private void fillData(BookModel bookModel) {
        mBookDetails = bookModel;

        mBookName.setText(bookModel.getName());
        mAuthor.setText(bookModel.getAuthor());
        mPublisher.setText(bookModel.getPublisher());
        mYear.setText(bookModel.getYearOfPublish());
        mCount.setText(bookModel.getBookCount());
        mDescription.setText(bookModel.getDescription());
        mISBN.setText(bookModel.getIsbn());

        if (Integer.parseInt(bookModel.getBookCount()) > 0) {
            mBookStatus.setTextColor(Color.GREEN);
            mBookStatus.setText("AVAILABLE");

            if (!Utility.isUserADMIN()) {
                mRequestBook.setEnabled(true);
            }
        } else {
            mBookStatus.setTextColor(Color.RED);
            mBookStatus.setText("NOT AVAILABLE");

            if (!Utility.isUserADMIN()) {
                mRequestBook.setEnabled(false);
            }
        }
    }

    private void enableViews(boolean isAdmin) {
        mBookName.setEnabled(isAdmin);
        mAuthor.setEnabled(isAdmin);
        mPublisher.setEnabled(isAdmin);
        mYear.setEnabled(isAdmin);
        mISBN.setEnabled(isAdmin);
        mCount.setEnabled(isAdmin);
        mDescription.setEnabled(isAdmin);
    }

    private String validData() {
        String msg = null;

        String value;

        if (Utility.isUserADMIN()) {
            value = mBookName.getText().toString();
            if (TextUtils.isEmpty(value)) {
                msg = "Enter Valid Book Name";
            }

            value = mAuthor.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Enter Valid Author Name";
            }

            value = mPublisher.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Enter Valid Publisher Name";
            }

            value = mYear.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Enter Valid Year of Publish";
            }

            value = mISBN.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Enter Valid ISBN No";
            }

            value = mCount.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Enter Valid Book Count";
            }

            value = mDescription.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Enter Valid Description";
            }
        } else {
            value = mStartDate.getText().toString();
            if (msg == null && TextUtils.isEmpty(value)) {
                msg = "Choose Start Date";
            }

            value = mEndDate.getText().toString();
            if (msg == null && (TextUtils.isEmpty(value) || value.equalsIgnoreCase("End Date"))) {
                msg = "Choose End Date";
            }
        }

        return msg;
    }

    /*
     * Date Picker Listener
     *
     * */
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            String dateString = Utility.FORMAT_DATE.format(new Date(calendar.getTimeInMillis()));

            // Update View also
            if (mDatePickerType == 1) {
                mStartYear = year;
                mStartMonth = month;
                mStartDay = day;
                mStartDateMillis = calendar.getTimeInMillis();

                mStartDate.setText(dateString);
            } else {
                mEndYear = year;
                mEndMonth = month;
                mEndDay = day;
                mEndDateMillis = calendar.getTimeInMillis();

                mEndDate.setText(dateString);
            }
        }
    };

    private Observer<BookListModel> bookDetailObserver = new Observer<BookListModel>() {
        @Override
        public void onChanged(BookListModel bookListModel) {
            if (bookListModel.getStatus().contains("fail")) {
                Toast.makeText(getContext(), "Failed to Fetch Book Details", Toast.LENGTH_SHORT).show();
            } else {
                fillData(bookListModel.getBookDetail().get(0));
            }
        }
    };

    private Observer<String> modifyBookObserver = new Observer<String>() {
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