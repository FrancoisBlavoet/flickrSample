package com.flickrtest.data.flickr.repo;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flickrtest.data.flickr.FlickrImage;
import com.flickrtest.data.flickr.FlickrImages;
import com.flickrtest.data.flickr.FlickrResult;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrImageRepository {

    private static final String TAG = FlickrImageRepository.class.getSimpleName();

    private final static int IMAGES_PER_PAGE = 100;
    public static final String BASE_URL = "https://api.flickr.com/services/rest/";

    private @NonNull final SearchEndPoint searchEndPoint;

    private Call<FlickrResult> currentCall;


    public interface SearchCallback {
        void onSearchResult(@NonNull List<FlickrImage> flickrImages, int max);

        void onSearchResultFailure();
    }


    public FlickrImageRepository(@NonNull SearchEndPoint searchEndPoint) {
        this.searchEndPoint = searchEndPoint;
    }


    public void queryPhotos(@NonNull String query,
                            @NonNull final SearchCallback callback,
                            int page) {
        currentCall = searchEndPoint.getPhotos(query, IMAGES_PER_PAGE, page);


        currentCall.enqueue(new Callback<FlickrResult>() {
            @Override
            @MainThread
            public void onResponse(Call<FlickrResult> call, Response<FlickrResult> response) {
                currentCall = null;
                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
                FlickrResult flickrResult = response.body();
                FlickrImages flickrImages = flickrResult.getFlickrImages();
                if (flickrImages == null) callback.onSearchResultFailure();
                else {
                    List<FlickrImage> flickrImage = flickrImages.getFlickrImage();
                    if (flickrImage == null) flickrImage = Collections.emptyList();
                    try {
                        int total = Integer.parseInt(flickrImages.getTotal());
                        callback.onSearchResult(flickrImage, total);
                    } catch (NumberFormatException ex) {
                        callback.onSearchResultFailure();
                    }
                }
            }

            @Override public void onFailure(Call<FlickrResult> call, Throwable t) {
                currentCall = null;
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    @MainThread
    public void cancelQuery() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }


}


