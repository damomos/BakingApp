package com.example.princess.bakingapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.fragments.StepsDetailActivityFragment;

public class StepsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepsDetailActivityFragment stepsDetailsFragment = new StepsDetailActivityFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.stepsdetailsframe, stepsDetailsFragment)
                    .commit();
        }
    }

}
