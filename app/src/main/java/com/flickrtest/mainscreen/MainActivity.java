package com.flickrtest.mainscreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.flickrtest.R;
import com.flickrtest.commons.BaseActivity;

import static com.flickrtest.utils.Activities.addFragmentToActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);


        FragmentManager fm = getSupportFragmentManager();
        MainScreenFragment fragment = (MainScreenFragment) fm.findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = MainScreenFragment.newInstance();
            addFragmentToActivity(fm, fragment, R.id.contentFrame);
        }

    }


}
