package com.flickrtest.mainscreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.flickrtest.commons.FragmentScope;
import com.flickrtest.commons.list.StatefulAdapter.ContentState;
import com.flickrtest.data.flickr.FlickrImage;
import com.flickrtest.data.flickr.repo.FlickrImageRepository;

import java.util.List;

import javax.inject.Inject;

import static android.text.TextUtils.isEmpty;
import static com.flickrtest.commons.list.StatefulAdapter.ContentState.EMPTY;
import static com.flickrtest.commons.list.StatefulAdapter.ContentState.LOADING;

@FragmentScope
public class MainScreenPresenter implements MainScreenContract.Presenter {

    private static final String TAG = MainScreenPresenter.class.getSimpleName();
    private static final String LAST_SEARCH_KEY = "search";

    private final @NonNull MainScreenContract.View view;
    private final @NonNull FlickrImageRepository flickrRepository;

    private List<FlickrImage> flickrImages;

    private String lastSearch;
    private int pageToQuery = 0;


    @Inject
    public MainScreenPresenter(@Nullable Bundle icicle,
                               @NonNull MainScreenContract.View view,
                               @NonNull FlickrImageRepository flickrRepository) {
        this.view = view;
        this.flickrRepository = flickrRepository;
        if (icicle == null) return;
        lastSearch = icicle.getString(LAST_SEARCH_KEY);
    }

    @Override public void start() {
        if (!isEmpty(lastSearch)) onQueryTextChange(lastSearch);
    }


    @Override public void saveState(@NonNull Bundle outState) {
        outState.putString(LAST_SEARCH_KEY, lastSearch);
        flickrRepository.cancelQuery();
    }

    @Inject void setup() {
        view.setPresenter(this);
    }

    private void queryFlickr(String query, FlickrImageRepository.SearchCallback callback) {
        Log.d(TAG, "queryFlickr: " + query);
        flickrRepository.queryPhotos(query, callback, pageToQuery);
    }


    //region callbacks


    @Override public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange() called with: " + "newText = [" + newText + "]");
        ContentState newState = isEmpty(newText) ? EMPTY : LOADING;
        view.setState(newState);
        lastSearch = newText;
        pageToQuery = 0;
        flickrRepository.cancelQuery();
        FlickrImageRepository.SearchCallback callback = new FlickrImageRepository.SearchCallback() {
            @Override public void onSearchResult(@NonNull List<FlickrImage> results, int max) {
                view.setMax(max);
                pageToQuery++;
                flickrImages = results;
                view.setPhotos(flickrImages);
            }

            @Override public void onSearchResultFailure() {

            }
        };
        queryFlickr(newText, callback);
        return true;
    }

    @Override public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit() called with: " + "query = [" + query + "]");
        // no need to start a new query, onTextChange has already been called
        view.saveRecentQuery(query);
        return true;
    }

    @Override public boolean onClose() {
        // let the widget reset its text
        pageToQuery = 0;
        return false;
    }


    @Override public void onNeedMoreItems() {
        Log.d(TAG, "onNeedMoreItems() called");
        pageToQuery++;
        queryFlickr(lastSearch, new FlickrImageRepository.SearchCallback() {
            @Override public void onSearchResult(@NonNull List<FlickrImage> results, int max) {
                flickrImages.addAll(results);
                view.notifyInsertion(results.size());
            }

            @Override public void onSearchResultFailure() { }
        });

    }
    //endregion

    @Override public void onRefreshButtonClick() { }


}


