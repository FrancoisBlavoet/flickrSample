package com.flickrtest.data.flickr.repo;


import com.flickrtest.data.flickr.FlickrResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchEndPoint {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1")
    Call<FlickrResult> getPhotos(@Query("text") String query,
                                 @Query("per_page") int perPage,
                                 @Query("page") int page);

}