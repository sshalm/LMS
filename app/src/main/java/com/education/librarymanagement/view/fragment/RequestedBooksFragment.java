package com.education.librarymanagement.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.adapter.RequestedBooksAdapter;
import com.education.librarymanagement.view.MainActivity;

/*
 * UI Class to hold Requested Books Fragment
 *
 * */
public class RequestedBooksFragment extends Fragment {

    private RecyclerView mBooksRequestedRecyclerView;
    private RequestedBooksAdapter mRequestedBooksAdapter;

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_requested_book, container, false);

        initializeViews(rootView);

        return rootView;
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mBooksRequestedRecyclerView = rootView.findViewById(R.id.rv_requestedbooks);
        mBooksRequestedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBooksRequestedRecyclerView.setHasFixedSize(true);

        mRequestedBooksAdapter = new RequestedBooksAdapter(bookClickedListener);

        mBooksRequestedRecyclerView.setAdapter(mRequestedBooksAdapter);
    }

    /*
     * Click Listener for List Items
     *
     * */
    private RequestedBooksAdapter.ItemClick bookClickedListener = new RequestedBooksAdapter.ItemClick() {
        @Override
        public void itemClicked(int position, int rentRequestId) {
            // Update Arguments for this Rent/Request Record to open
            Bundle data = new Bundle();
            data.putInt("", rentRequestId);

            // Open Detail Fragment to View/Update this Rent/Request Record
            ((MainActivity) getActivity()).openFragment(MainActivity.FRAG_RENT_REQUEST_DETAIL, data);
        }
    };
}