package com.education.librarymanagement.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.model.BookModel;
import com.education.librarymanagement.model.RentModel;
import com.education.librarymanagement.utils.Utility;
import com.education.librarymanagement.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

/*
 * Adapter class to display Books on Rent
 *
 * */
public class RentedBooksAdapter extends RecyclerView.Adapter<RentedBooksAdapter.RentHolder> {

    private List<RentModel> rentList;
    private MainActivity.ItemClick itemClickListener;

    // Constructor to initialize List Item's Click Listener
    public RentedBooksAdapter(MainActivity.ItemClick clickListener) {
        rentList = new ArrayList<>();
        this.itemClickListener = clickListener;
    }

    // Update Rent List for this Adapter
    public void addRentList(List<RentModel> rentList) {
        this.rentList = rentList;
    }

    // Total Number of Rent Records available
    @Override
    public int getItemCount() {
        return rentList != null ? rentList.size() : 0;
    }

    // Create View for this List Item
    @Override
    public RentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rented_book, parent, false);
        return new RentHolder(view);
    }

    // View holder for Rent Record Item
    static class RentHolder extends RecyclerView.ViewHolder {

        Button returnBook;
        TextView count, name;
        TextView user, startDate, dueDate;

        RentHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_item_rentedbook_name);
            count = itemView.findViewById(R.id.tv_item_rentedbook_count);
            user = itemView.findViewById(R.id.tv_item_rentedbook_username);
            dueDate = itemView.findViewById(R.id.tv_item_rentedbook_duedate);
            returnBook = itemView.findViewById(R.id.bt_item_rentedbook_return);
            startDate = itemView.findViewById(R.id.tv_item_rentedbook_rentaldate);
        }
    }

    // Bind individual Rent Record's data to Recycler View
    @Override
    public void onBindViewHolder(final RentHolder holder, @SuppressLint("RecyclerView") final int position) {
        final RentModel rentDetail = rentList.get(position);

        holder.count.setText((position + 1) + "");
        holder.name.setText(rentDetail.getName());
        holder.user.setText(rentDetail.getFirstName() + " " + rentDetail.getLastName());
        holder.startDate.setText(rentDetail.getIssueDate());
        holder.dueDate.setText(rentDetail.getRenewalDate());

        if (Utility.isUserADMIN()) {
            holder.returnBook.setVisibility(View.VISIBLE);

            holder.returnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Inform Fragment that a List Item is clicked
                    itemClickListener.itemClicked(position, rentDetail.getRentId());
                }
            });
        } else {
            holder.returnBook.setVisibility(View.GONE);
        }
    }
}