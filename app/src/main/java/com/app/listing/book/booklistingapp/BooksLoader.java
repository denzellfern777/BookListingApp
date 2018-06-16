package com.app.listing.book.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class BooksLoader extends AsyncTaskLoader<List<Book>> {

    private final String mUrl;


    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return NetworkUtils.fetchBookData(mUrl);

    }
}
