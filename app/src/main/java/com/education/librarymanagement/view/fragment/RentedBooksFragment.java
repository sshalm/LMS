package com.education.librarymanagement.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.adapter.RentedBooksAdapter;
import com.education.librarymanagement.model.RentListModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;
import com.education.librarymanagement.viewmodel.BooksViewModel;

/*
 * UI Class to hold Rented Books Fragment
 *
 * */
public class RentedBooksFragment extends Fragment {

    private BooksViewModel booksViewModel;
    private RecyclerView mBooksRentedRecyclerView;
    private RentedBooksAdapter mRentedBooksAdapter;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        booksViewModel = ViewModelProviders.of(RentedBooksFragment.this).get(BooksViewModel.class);
    }

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_rented_book, container, false);

        initializeViews(rootView);
        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Rented Books");
    }

    private void initData() {
        if (Utility.isUserADMIN()) {
            booksViewModel.rentList().observe(getViewLifecycleOwner(), rentDetailObserver);
        } else {
            booksViewModel.getRentedBooks(Utility.getLoggedInUser().getUserId())
                    .observe(getViewLifecycleOwner(), rentDetailObserver);
        }
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mBooksRentedRecyclerView = rootView.findViewById(R.id.rv_rentedbooks);
        mBooksRentedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBooksRentedRecyclerView.setHasFixedSize(true);

        mRentedBooksAdapter = new RentedBooksAdapter(returnBookListener);

        mBooksRentedRecyclerView.setAdapter(mRentedBooksAdapter);
    }

    /*
     * Click Listener for List Items
     *
     * */
    private MainActivity.ItemClick returnBookListener = new MainActivity.ItemClick() {
        @Override
        public void itemClicked(int position, int rentRequestId) {

        }
    };

    private Observer<RentListModel> rentDetailObserver = new Observer<RentListModel>() {
        @Override
        public void onChanged(RentListModel rentListModel) {
            if (rentListModel.getStatus().contains("fail")) {
                Toast.makeText(getContext(), "Failed to Fetch Rented Books Details", Toast.LENGTH_SHORT).show();
            } else {
                mRentedBooksAdapter.addRentList(rentListModel.getRentDetail());
                mRentedBooksAdapter.notifyDataSetChanged();
            }
        }
    };

    private Observer<String> returnBookObserver = new Observer<String>() {
        @Override
        public void onChanged(String response) {
            if (response.contains("fail")) {
                Toast.makeText(getContext(), "Failed to Return Rented Book", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    };
}