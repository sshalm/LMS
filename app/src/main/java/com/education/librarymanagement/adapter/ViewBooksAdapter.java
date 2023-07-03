package com.education.librarymanagement.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.education.librarymanagement.R;
import com.education.librarymanagement.model.BookModel;

import java.util.ArrayList;
import java.util.List;

/*
 * Adapter class to display Books
 *
 * */
public class ViewBooksAdapter extends RecyclerView.Adapter<ViewBooksAdapter.BookHolder> {

    private List<BookModel> booksList;
    private ItemClick itemClickListener;

    // Constructor to initialize List Item's Click Listener
    public ViewBooksAdapter(ItemClick clickListener) {
        booksList = new ArrayList<>();
        this.itemClickListener = clickListener;
    }

    // Update Books List for this Adapter
    public void addBooksList(List<BookModel> booksList) {
        this.booksList = booksList;
    }

    // Total Number of Book Records available
    @Override
    public int getItemCount() {
        return booksList != null ? booksList.size() : 0;
    }

    // Create View for this List Item
    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_book, parent, false);
        return new BookHolder(view);
    }

    // View holder for Book Record Item
    static class BookHolder extends RecyclerView.ViewHolder {

        LinearLayout outerView;
        TextView author, publisher;
        TextView bookCover, bookName;

        BookHolder(View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.tv_item_viewbook_author);
            bookName = itemView.findViewById(R.id.tv_item_viewbook_name);
            bookCover = itemView.findViewById(R.id.tv_item_viewbook_cover);
            publisher = itemView.findViewById(R.id.tv_item_viewbook_publisher);
            outerView = itemView.findViewById(R.id.ll_item_viewbook_outerview);
        }
    }

    // Bind individual Book Record's data to Recycler View
    @Override
    public void onBindViewHolder(final BookHolder holder, @SuppressLint("RecyclerView") final int position) {
        final BookModel bookDetail = booksList.get(position);

        String coverName = "";
        String[] splitArray = bookDetail.getName().split(" ");
        if (splitArray.length > 0) {
            for (int i = 0; i < splitArray.length && i < 3; i++) {
                coverName += splitArray[i].charAt(0);
            }
        }

        holder.author.setText(bookDetail.getAuthor());
        holder.bookName.setText(bookDetail.getName());
        holder.bookCover.setText(coverName.toUpperCase());
        holder.publisher.setText(bookDetail.getPublisher());
        holder.outerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inform Fragment that a List Item is clicked
                itemClickListener.itemClicked(position, booksList.get(position).getBookId());
            }
        });
    }

    /*
     * Interface that handle clicks on List's Items
     * */
    public interface ItemClick {
        void itemClicked(int position, int bookId);
    }
}