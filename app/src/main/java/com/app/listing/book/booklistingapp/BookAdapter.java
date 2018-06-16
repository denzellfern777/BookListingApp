package com.app.listing.book.booklistingapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> Books) {
        super(context, 0, Books);
    }

    @NonNull
    @Override
    public View getView(int position, View listItemView, @NonNull ViewGroup parent) {

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        final Book currentBook = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.book_title);
        TextView authorTextView = listItemView.findViewById(R.id.book_author);

        assert currentBook != null;

        titleTextView.setText(currentBook.getTitle());
        authorTextView.setText(currentBook.getAuthor());


        return listItemView;

    }
}
