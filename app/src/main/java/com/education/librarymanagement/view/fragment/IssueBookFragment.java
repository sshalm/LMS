package com.education.librarymanagement.view.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.education.librarymanagement.model.UserListModel;
import com.education.librarymanagement.model.UserModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;
import com.education.librarymanagement.viewmodel.BooksViewModel;
import com.education.librarymanagement.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * UI Class to hold Issue Book Fragment
 *
 * */
public class IssueBookFragment extends Fragment {

    private int mDatePickerType;
    private int mEndYear, mEndMonth, mEndDay;
    private int mStartYear, mStartMonth, mStartDay;

    private long mStartDateMillis, mEndDateMillis;

    private LinearLayout mBookDetailsView;
    private Spinner mSelectUser, mSelectBook;
    private Button mStartDate, mEndDate, mIssueBook;
    private TextView mBookDescription, mBookAuthor, mBookPublihser;

    // View Model Object to interact with Business Logic
    private UserViewModel usersViewModel;
    private BooksViewModel booksViewModel;

    private List<UserModel> userList;
    private List<BookModel> bookList;
    private List<String> userNames, bookNames;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        usersViewModel = ViewModelProviders.of(IssueBookFragment.this).get(UserViewModel.class);
        booksViewModel = ViewModelProviders.of(IssueBookFragment.this).get(BooksViewModel.class);
    }

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_issue_book, container, false);

        initializeViews(rootView);

        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Issue Book");
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mSelectUser = rootView.findViewById(R.id.sp_issuebook_user);
        mSelectBook = rootView.findViewById(R.id.sp_issuebook_selectbook);
        mBookDescription = rootView.findViewById(R.id.tv_issuebook_bookdescription);
        mBookAuthor = rootView.findViewById(R.id.tv_issuebook_bookauthor);
        mBookPublihser = rootView.findViewById(R.id.tv_issuebook_bookpublisher);
        mStartDate = rootView.findViewById(R.id.bt_issuebook_startdate);
        mEndDate = rootView.findViewById(R.id.bt_issuebook_enddate);
        mIssueBook = rootView.findViewById(R.id.bt_issuebook_issue);
        mBookDetailsView = rootView.findViewById(R.id.ll_issuebook_bookdetails);

        mSelectBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (bookList.size() > i) {
                    BookModel selectedBook = bookList.get(i);

                    mBookDescription.setText("DESCRIPTION : " + selectedBook.getDescription());
                    mBookAuthor.setText("AUTHOR : " + selectedBook.getAuthor());
                    mBookPublihser.setText("PUBLISHER : " + selectedBook.getPublisher());

                    mBookDetailsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        mIssueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String errMsg = validData();
                if (errMsg != null) {
                    Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = userList.get(mSelectUser.getSelectedItemPosition()).getUserId();
                int bookId = bookList.get(mSelectBook.getSelectedItemPosition()).getBookId();

                booksViewModel.issueBook(userId, bookId, Utility.FORMAT_DATE_TIME.format(mStartDateMillis),
                        Utility.FORMAT_DATE_TIME.format(mEndDateMillis))
                        .observe(getViewLifecycleOwner(), issueBookObserver);
            }
        });
    }

    private String validData() {
        String msg = null;

        String value = (String) mSelectUser.getSelectedItem();
        if (TextUtils.isEmpty(value)) {
            msg = "Select a User";
        }

        value = (String) mSelectBook.getSelectedItem();
        if (msg == null && TextUtils.isEmpty(value)) {
            msg = "Select a Book";
        }

        value = mStartDate.getText().toString();
        if (msg == null && TextUtils.isEmpty(value)) {
            msg = "Choose Start Date";
        }

        value = mEndDate.getText().toString();
        if (msg == null && (TextUtils.isEmpty(value) || value.equalsIgnoreCase("End Date"))) {
            msg = "Choose End Date";
        }

        return msg;
    }

    private void initData() {
        userNames = new ArrayList<>();
        bookNames = new ArrayList<>();

        usersViewModel.userList().observe(getViewLifecycleOwner(), userListObserver);
        booksViewModel.bookList().observe(getViewLifecycleOwner(), bookListObserver);

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

    /*
     * Click Listener for List Items
     *
     * */
    private MainActivity.ItemClick bookClickedListener = new MainActivity.ItemClick() {
        @Override
        public void itemClicked(int position, int rentRequestId) {
            // Update Arguments for this Rent/Request Record to open
            Bundle data = new Bundle();
            data.putInt("", rentRequestId);

            // Open Detail Fragment to View/Update this Rent/Request Record
            ((MainActivity) getActivity()).openFragment(MainActivity.FRAG_RENT_REQUEST_DETAIL, data);
        }
    };

    private Observer<UserListModel> userListObserver = new Observer<UserListModel>() {
        @Override
        public void onChanged(UserListModel userListModel) {
            if (userListModel.getStatus().contains("fail")) {
                Toast.makeText(getContext(), "Failed to Fetch Users", Toast.LENGTH_SHORT).show();
            } else {
                userList = userListModel.getUserDetail();
                for (UserModel user : userList) {
                    userNames.add(user.getFirstName() + " " + user.getLastName());
                }

                ArrayAdapter<String> userNameAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, userNames);
                mSelectUser.setAdapter(userNameAdapter);
            }
        }
    };

    private Observer<BookListModel> bookListObserver = new Observer<BookListModel>() {
        @Override
        public void onChanged(BookListModel bookListModel) {
            if (bookListModel.getStatus().contains("fail")) {
                Toast.makeText(getContext(), "Failed to Fetch Books", Toast.LENGTH_SHORT).show();
            } else {
                bookList = bookListModel.getBookDetail();
                for (BookModel book : bookList) {
                    bookNames.add(book.getName());
                }

                ArrayAdapter<String> bookNameAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, bookNames);
                mSelectBook.setAdapter(bookNameAdapter);
            }
        }
    };

    private Observer<String> issueBookObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            if (message.equals("success")) {
                BookModel bookModel = bookList.get(mSelectBook.getSelectedItemPosition());
                bookModel.setBookCount((Integer.parseInt(bookModel.getBookCount()) - 1) + "");

                // Now Reduce Book Count in DB
                booksViewModel.updateBook(bookModel).observe(getViewLifecycleOwner(), modifyBookObserver);
            }
        }
    };

    private Observer<String> modifyBookObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            String toastMessage = "Issue Book ";

            if (message.contains("success")) {
                toastMessage += "Success";

                Message msg = new Message();
                msg.what = 2;
                ((MainActivity) getActivity()).postMessage(msg, 500);
            } else {
                toastMessage += "Fail";
            }

            Toast.makeText(getContext(), toastMessage.toUpperCase(), Toast.LENGTH_SHORT).show();
        }
    };
}