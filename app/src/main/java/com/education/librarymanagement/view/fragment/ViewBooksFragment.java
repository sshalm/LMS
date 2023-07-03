package com.education.librarymanagement.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.adapter.ViewBooksAdapter;
import com.education.librarymanagement.model.BookListModel;
import com.education.librarymanagement.model.BookModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;
import com.education.librarymanagement.viewmodel.BooksViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/*
 * UI Class to hold View Books Fragment
 *
 * */
public class ViewBooksFragment extends Fragment {

    // View Model Object to interact with Business Logic
    private BooksViewModel booksViewModel;

    private Spinner mSearchBy;
    private TextInputEditText mSearchText;
    private RecyclerView mBooksRecyclerView;
    private ViewBooksAdapter mViewBookAdapter;

    private List<String> mSearchTags;
    private List<BookModel> mParentList, mChildList;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        booksViewModel = ViewModelProviders.of(ViewBooksFragment.this).get(BooksViewModel.class);
    }

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_books, container, false);

        initializeViews(rootView);
        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("View Books");
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mSearchText = rootView.findViewById(R.id.et_viewbook_searchname);
        mSearchBy = rootView.findViewById(R.id.sp_viewbook_spinner);

        mSearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == (mSearchTags.size() - 1)) {
                    // OPen Camera to Scan BarCode
                    IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                    intentIntegrator.setPrompt("Scan a barcode or QR Code");
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.initiateScan();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mParentList != null && mParentList.size() > 0) {
                    mChildList = new ArrayList<>();

                    String searchFor = charSequence.toString().toLowerCase();

                    for (BookModel book : mParentList) {
                        String searchIn;

                        switch (mSearchBy.getSelectedItemPosition()) {

                            case Utility.TAG_AUTHOR_NAME:
                                searchIn = book.getAuthor();
                                break;

                            case Utility.TAG_PUBLISHER_NAME:
                                searchIn = book.getPublisher();
                                break;

                            case Utility.TAG_ISBN_NO:
                            case Utility.TAG_SCAN_ISBN:
                                searchIn = book.getIsbn();
                                break;

                            default:
                            case Utility.TAG_BOOK_NAME:
                                searchIn = book.getName();
                                break;
                        }

                        if (searchIn.toLowerCase().contains(searchFor)) {
                            mChildList.add(book);
                        }
                    }

                    mViewBookAdapter.addBooksList(mChildList);
                    mViewBookAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBooksRecyclerView = rootView.findViewById(R.id.rv_viewbooks);
        mBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBooksRecyclerView.setHasFixedSize(true);

        mViewBookAdapter = new ViewBooksAdapter(bookClickedListener);

        mBooksRecyclerView.setAdapter(mViewBookAdapter);
    }

    private void initData() {
        booksViewModel.bookList().observe(getViewLifecycleOwner(), bookListObserver);
    }

    /*
     * Click Listener for List Items
     *
     * */
    private ViewBooksAdapter.ItemClick bookClickedListener = new ViewBooksAdapter.ItemClick() {
        @Override
        public void itemClicked(int position, int bookId) {
            // Update Arguments for this Book Record to open
            Bundle data = new Bundle();
            data.putInt("book_id", bookId);

            // Open Detail Fragment to View/Update/Delete this Book Record
            ((MainActivity) getActivity()).openFragment(MainActivity.FRAG_BOOK_DETAIL, data);
        }
    };

    private Observer<BookListModel> bookListObserver = new Observer<BookListModel>() {
        @Override
        public void onChanged(BookListModel bookListModel) {
            if (bookListModel.getStatus().contains("fail")) {
                Toast.makeText(getContext(), "Failed to Fetch Books", Toast.LENGTH_SHORT).show();

                mViewBookAdapter.addBooksList(null);
                mViewBookAdapter.notifyDataSetChanged();
            } else {
                mParentList = bookListModel.getBookDetail();

                mViewBookAdapter.addBooksList(mParentList);
                mViewBookAdapter.notifyDataSetChanged();

                mSearchTags = new ArrayList<>();
                mSearchTags.add("BOOK NAME");
                mSearchTags.add("AUTHOR NAME");
                mSearchTags.add("PUBLISHER NAME");
                mSearchTags.add("ISBN NO");
                mSearchTags.add("SCAN ISBN NO");

                ArrayAdapter<String> searchTagAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, mSearchTags);
                mSearchBy.setAdapter(searchTagAdapter);
            }
        }
    };

    public void updateScannerResult(String scannedISBN) {
        mSearchText.setText(scannedISBN);
    }
}