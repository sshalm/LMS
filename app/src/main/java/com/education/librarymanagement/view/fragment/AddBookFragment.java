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
import com.education.librarymanagement.model.BookModel;
import com.education.librarymanagement.view.MainActivity;
import com.education.librarymanagement.viewmodel.BooksViewModel;
import com.google.android.material.textfield.TextInputEditText;

/*
 * UI Class to hold Login Fragment
 *
 * */
public class AddBookFragment extends Fragment {

    // View Model Object to interact with Business Logic
    private BooksViewModel booksViewModel;

    private Button mAddBook;
    private TextInputEditText mBookName, mAuthor;
    private TextInputEditText mCount, mDescription;
    private TextInputEditText mISBN, mPublisher, mYear;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        booksViewModel = ViewModelProviders.of(AddBookFragment.this).get(BooksViewModel.class);
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
                R.layout.fragment_add_book, container, false);

        initializeViews(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Add Book");
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mBookName = rootView.findViewById(R.id.et_addbook_name);
        mAuthor = rootView.findViewById(R.id.et_addbook_author);
        mPublisher = rootView.findViewById(R.id.et_addbook_publisher);
        mYear = rootView.findViewById(R.id.et_addbook_year);
        mISBN = rootView.findViewById(R.id.et_addbook_isbn);
        mDescription = rootView.findViewById(R.id.et_addbook_description);
        mCount = rootView.findViewById(R.id.et_addbook_count);
        mAddBook = rootView.findViewById(R.id.bt_addbook_add);

        mAddBook.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View view) {
                String errMsg = validData();
                if (errMsg != null) {
                    Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                BookModel bookModel = new BookModel(mBookName.getText().toString(),
                        mAuthor.getText().toString(), mPublisher.getText().toString(),
                        mISBN.getText().toString(), mYear.getText().toString(),
                        mCount.getText().toString(), mDescription.getText().toString());

                booksViewModel.addBook(bookModel).observe(getViewLifecycleOwner(), addBookObserver);
            }
        });
    }

    private Observer<String> addBookObserver = new Observer<String>() {
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

    private String validData() {
        String msg = null;

        String value = mBookName.getText().toString();
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

        return msg;
    }
}