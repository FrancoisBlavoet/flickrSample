package com.flickrtest.application;


import com.flickrtest.data.flickr.repo.FlickrImageRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    LostApplication getApplication();

    FlickrImageRepository getflickrRepository();
}
