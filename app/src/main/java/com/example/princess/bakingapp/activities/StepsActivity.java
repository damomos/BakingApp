package com.example.princess.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.fragments.StepsActivityFragment;
import com.example.princess.bakingapp.fragments.StepsDetailActivityFragment;

import static com.example.princess.bakingapp.activities.MainActivity.isTablet;

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(isTablet){
                StepsActivityFragment stepsActivityFragment = new StepsActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.stepsframe, stepsActivityFragment)
                        .commit();

                StepsDetailActivityFragment stepsDetailActivityFragment = new StepsDetailActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.stepsdetailsframe, stepsDetailActivityFragment)
                        .commit();
            } else {
                StepsActivityFragment stepsActivityFragment = new StepsActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.stepsframe, stepsActivityFragment)
                        .commit();
            }
        }

    }

}
