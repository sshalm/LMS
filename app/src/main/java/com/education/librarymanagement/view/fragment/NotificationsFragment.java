package com.education.librarymanagement.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.adapter.NotificationsAdapter;
import com.education.librarymanagement.model.NotificationListModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.viewmodel.BooksViewModel;
import com.google.android.material.textfield.TextInputEditText;

/*
 * UI Class to hold Notifications Fragment
 *
 * */
public class NotificationsFragment extends Fragment {

    private BooksViewModel booksViewModel;

    private Button mSendNotification;
    private LinearLayout mSendNotificationView;
    private TextInputEditText mNotificationMessage;
    private RecyclerView mNotificationsRecyclerView;

    private NotificationsAdapter mNotificationsAdapter;

    // Android Callback when Fragment is Created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Model
        booksViewModel = ViewModelProviders.of(NotificationsFragment.this).get(BooksViewModel.class);
    }

    /*
     * Android Callback when Fragment's View is Created
     * Initialize all the View Objects used
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, container, false);

        initializeViews(rootView);
        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Notifications");
    }

    private void initData() {
        int userId = 0;

        if (!Utility.isUserADMIN()) {
            userId = Utility.getLoggedInUser().getUserId();
        }

        booksViewModel.getNotifications(userId).observe(getViewLifecycleOwner(), notificationObserver);
    }

    /*
     * Load UI components for Fragment
     *
     * @rootView: Fragment's Parent UI Object
     *
     * */
    private void initializeViews(View rootView) {
        mSendNotificationView = rootView.findViewById(R.id.ll_notifications_outer);

        if (Utility.isUserADMIN()) {
            mSendNotificationView.setVisibility(View.VISIBLE);

            mSendNotification = rootView.findViewById(R.id.bt_notifications_send);
            mNotificationMessage = rootView.findViewById(R.id.et_notifications_message);

            mSendNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = mNotificationMessage.getText().toString();

                    if (message == null || TextUtils.isEmpty(message)) {
                        Toast.makeText(getActivity(), "Enter Notification Message", Toast.LENGTH_SHORT).show();
                    }

                    booksViewModel.sendNotification(0, message).observe(getViewLifecycleOwner(), sendNotificationObserver);
                }
            });
        } else {
            mSendNotificationView.setVisibility(View.GONE);
        }

        mNotificationsRecyclerView = rootView.findViewById(R.id.rv_notifications);
        mNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNotificationsRecyclerView.setHasFixedSize(true);

        mNotificationsAdapter = new NotificationsAdapter();

        mNotificationsRecyclerView.setAdapter(mNotificationsAdapter);
    }

    private Observer<NotificationListModel> notificationObserver = new Observer<NotificationListModel>() {
        @Override
        public void onChanged(NotificationListModel notificationListModel) {
            if (notificationListModel.getStatus().contains("fail")) {
                Toast.makeText(getContext(), "Failed to Fetch Notifications", Toast.LENGTH_SHORT).show();
            } else {
                mNotificationsAdapter.addNotificationList(notificationListModel.getNotificationDetail());
                mNotificationsAdapter.notifyDataSetChanged();
            }
        }
    };

    private Observer<String> sendNotificationObserver = new Observer<String>() {
        @Override
        public void onChanged(String response) {
            if (response.contains("fail")) {
                Toast.makeText(getContext(), "Failed to Send Notification", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    };
}