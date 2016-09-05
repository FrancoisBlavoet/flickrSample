package com.flickrtest.mainscreen;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import com.flickrtest.commons.BasePresenter;
import com.flickrtest.commons.BaseView;
import com.flickrtest.commons.list.EmptyListCallback;
import com.flickrtest.commons.list.InfiniteAdapterCallback;
import com.flickrtest.commons.list.StatefulAdapter;
import com.flickrtest.data.flickr.FlickrImage;
import com.flickrtest.utils.SearchViewBindingAdapter;

import java.util.List;


public interface MainScreenContract {

    interface View extends BaseView<Presenter> {

        void setPhotos(@NonNull List<FlickrImage> flickrImages);

        void setState(StatefulAdapter.ContentState loading);

        void setMax(int max);

        void notifyInsertion(int size);

        void saveRecentQuery(String query);
    }

    interface Presenter extends BasePresenter,
                                EmptyListCallback,
                                InfiniteAdapterCallback,
                                SearchViewBindingAdapter.OnQueryTextChange,
                                SearchViewBindingAdapter.OnQueryTextSubmit,
                                SearchView.OnCloseListener {

    }
}
