package com.flickrtest.application;

import com.flickrtest.BuildConfig;
import com.flickrtest.data.flickr.repo.FlickrImageRepository;
import com.flickrtest.data.flickr.repo.SearchEndPoint;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.flickrtest.data.flickr.repo.FlickrImageRepository.BASE_URL;

@Module
public final class ApplicationModule {

    private static final String FLICKR_CLIENT = "flickrClient";

    private final LostApplication lostApplication;

    public ApplicationModule(LostApplication lostApplication) {
        this.lostApplication = lostApplication;
    }


    @Provides
    @Singleton
    public LostApplication provideApplication() {
        return lostApplication;
    }


    @Singleton
    @Provides
    public FlickrImageRepository provideFlickrRepository(SearchEndPoint searchEndPoint) {
        return new FlickrImageRepository(searchEndPoint);
    }


    @Provides Retrofit provideRetrofit(@Named(FLICKR_CLIENT) OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Named(FLICKR_CLIENT)
    @Provides OkHttpClient provideClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter(
                        "api_key", BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();
        return okHttpClient;
    }

    @Provides SearchEndPoint provideSearch(Retrofit retrofit) {
        return retrofit.create(SearchEndPoint.class);
    }

}
