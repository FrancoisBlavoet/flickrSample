package com.flickrtest.commons;


import android.os.Bundle;
import android.support.annotation.NonNull;

public interface BasePresenter {

    /**
     * Called when the view coupled with this presenter is displayed for the first time
     */
    void start();

    /**
     * The view is being destroyed, time to save the state of the presenter
     */
    void saveState(@NonNull Bundle outState);
}
