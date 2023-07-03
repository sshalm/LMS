package com.education.librarymanagement.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.model.NotificationModel;
import com.education.librarymanagement.model.RentModel;

import java.util.ArrayList;
import java.util.List;

/*
 * Adapter class to display Notifications
 *
 * */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {

    private List<NotificationModel> notificationList;

    public NotificationsAdapter() {
        notificationList = new ArrayList<>();
    }

    // Update Notifications List for this Adapter
    public void addNotificationList(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    // Total Number of Notifications available
    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    // Create View for this List Item
    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);
        return new NotificationHolder(view);
    }

    // View holder for Notification Item
    static class NotificationHolder extends RecyclerView.ViewHolder {

        TextView count, message;

        NotificationHolder(View itemView) {
            super(itemView);

            count = itemView.findViewById(R.id.tv_item_notification_no);
            message = itemView.findViewById(R.id.tv_item_notification_message);
        }
    }

    // Bind individual Notification data to Recycler View
    @Override
    public void onBindViewHolder(final NotificationHolder holder, @SuppressLint("RecyclerView") final int position) {
        final NotificationModel notification = notificationList.get(position);

        holder.count.setText((position + 1) + "");
        holder.message.setText(notification.getDescription());
    }
}