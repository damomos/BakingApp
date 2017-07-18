package com.example.princess.bakingapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.fragments.StepsActivityFragment;
import com.example.princess.bakingapp.fragments.StepsDetailActivityFragment;
import com.example.princess.bakingapp.model.Recipes;

import java.util.ArrayList;

import static com.example.princess.bakingapp.activities.MainActivity.isTablet;
import static com.example.princess.bakingapp.fragments.MainActivityFragment.bakes;

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
