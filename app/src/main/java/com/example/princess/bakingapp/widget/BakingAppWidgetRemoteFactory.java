package com.example.princess.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.model.Recipes;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.princess.bakingapp.fragments.MainActivityFragment.bakes;

/**
 * Created by Princess on 7/18/2017.
 */

public class BakingAppWidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Recipes> recipes;
    private Context context;

    public BakingAppWidgetRemoteFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (recipes == null) {

            final String UDACITY_RECIPE_BASE_URL = "https://go.udacity.com/android-baking-app-json";

            Uri builtUri = Uri.parse(UDACITY_RECIPE_BASE_URL);
            HttpURLConnection httpURLConnection = null;
            try {
                //Get url
                URL url = new URL(builtUri.toString());
                //Open connection
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // create an input stream reader
                InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream());

                Recipes [] recipesArray = new Gson().fromJson(reader, Recipes[].class);
                recipes = new ArrayList<>(Arrays.asList(recipesArray));

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        recipes.clear();
    }

    @Override
    public int getCount() {
        if(recipes == null)
            return 0;

        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        //Update the pie image
        remoteViews.setImageViewResource(R.id.widget_bake_image, R.drawable.brownies);

        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.widget_bake_image, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
