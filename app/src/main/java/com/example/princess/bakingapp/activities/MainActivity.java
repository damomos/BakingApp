package com.example.princess.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity {

    public static boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            if(findViewById(R.id.tablet_view) != null){
                isTablet = true;

                FragmentManager fragmentManager = getSupportFragmentManager();
                MainActivityFragment mainActivityFragment = new MainActivityFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.tablet_view, mainActivityFragment)
                        .commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainActivityFragment mainActivityFragment = new MainActivityFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.phone_view, mainActivityFragment)
                        .commit();
            }
        }
    }

}
