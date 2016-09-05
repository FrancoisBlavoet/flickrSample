package com.flickrtest.mainscreen;


import android.content.SearchRecentSuggestionsProvider;

public class FlickrSearchRecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = FlickrSearchRecentSuggestionsProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public FlickrSearchRecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}