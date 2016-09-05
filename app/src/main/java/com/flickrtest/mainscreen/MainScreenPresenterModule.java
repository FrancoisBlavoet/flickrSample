package com.flickrtest.mainscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.flickrtest.data.flickr.repo.FlickrImageRepository;

import dagger.Module;
import dagger.Provides;


@Module
public class MainScreenPresenterModule {

    private final MainScreenContract.View view;
    @Nullable private Bundle icicle;

    public MainScreenPresenterModule(MainScreenContract.View view,
                                     @Nullable Bundle icicle) {
        this.view = view;
        this.icicle = icicle;
    }

    @Provides
    MainScreenContract.View provideView() {
        return view;
    }

    @Provides
    MainScreenContract.Presenter providePresenter(FlickrImageRepository flickrRepository) {
        return new MainScreenPresenter(icicle, view, flickrRepository);
    }


}


