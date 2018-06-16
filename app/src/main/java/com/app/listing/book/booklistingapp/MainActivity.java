package com.app.listing.book.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    private ListView booksListView;
    private EditText searchField;
    private TextView emptyTextView;
    private String requestGoogleBooksUrl = "";
    private View loadingProgressBar;
    private BookAdapter adapter;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        booksListView = findViewById(R.id.books_list);
        adapter = new BookAdapter(this, new ArrayList<Book>());
        booksListView.setAdapter(adapter);

        emptyTextView = findViewById(R.id.empty_text_view);
        booksListView.setEmptyView(emptyTextView);

        loadingProgressBar = findViewById(R.id.loading);

        ImageView searchButton = findViewById(R.id.search_btn);

        searchField = findViewById(R.id.search_field);


        final ConnectivityManager connMgr =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            loadingProgressBar.setVisibility(View.GONE);

            emptyTextView.setText(R.string.no_internet);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    updateQueryUrl(searchField.getText().toString());
                    emptyTextView.setVisibility(View.GONE);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    adapter.clear();
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(R.string.no_internet);
                }
            }
        });


    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        updateQueryUrl(searchField.getText().toString());
        return new BooksLoader(this, requestGoogleBooksUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        View loading_spinner_progressbar = findViewById(R.id.loading);
        loading_spinner_progressbar.setVisibility(View.GONE);
        if (isFirstTime) {
            isFirstTime = false;
        } else {
            emptyTextView.setText(R.string.no_books);
        }
        adapter.clear();

        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }

    private void updateQueryUrl(String searchValue) {

        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        requestGoogleBooksUrl = "https://www.googleapis.com/books/v1/volumes?q=" + searchValue + "&maxResults=15";
    }
}
