package com.example.princess.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.activities.StepsActivity;
import com.example.princess.bakingapp.adapter.RecipeAdapter;
import com.example.princess.bakingapp.model.Recipes;


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.princess.bakingapp.activities.MainActivity.isTablet;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private LinearLayout noInternet;
    RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Recipes> bakes = new ArrayList<>();


    private final String PORTRAIT_VIEW_STATE = "portrait_view_state";
    private final String LANDSCAPE_VIEW_STATE = "landscape_view_state";
    private static Parcelable BundlePortraitViewState;
    private static Parcelable BundleLandscapeViewState;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        noInternet = (LinearLayout) view.findViewById(R.id.empty_state_container);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView = (RecyclerView)view.findViewById(R.id.recipe_list);

        downloadRecipes();
        initViews();
        return view;
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void initViews(){
        if(isTablet){
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {

            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                layoutManager = new LinearLayoutManager(getActivity());
            }else {

                layoutManager = new GridLayoutManager(getActivity(), 2);
            }
        }


        recyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(this, bakes);
        recyclerView.setAdapter(recipeAdapter);
    }


    public class  FetchRecipeTask extends AsyncTask<Void, Void, ArrayList<Recipes>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Recipes> doInBackground(Void... params) {


            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            final String UDACITY_RECIPE_BASE_URL = "https://go.udacity.com/android-baking-app-json";

            try {
                Uri builtUri = Uri.parse(UDACITY_RECIPE_BASE_URL)
                        .buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                //Giving a url a open connection
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                //Openstream method of getting call from web
                InputStream inputStream = httpURLConnection.getInputStream();

                //Set the connection timeout to 5 seconds and the read timeout to 10 seconds
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(10000);

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                //Get a stream to read data from
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONArray movieArray = new JSONArray(buffer.toString());
                bakes = new ArrayList<>();
                for (int i = 0; i < movieArray.length(); i++) {
                    bakes.add(new Recipes(movieArray.getJSONObject(i)));
                    Log.e("name: ", bakes.get(i).getName());
                }
                return bakes;
            } catch (Exception e) {
                e.printStackTrace();
                return bakes;
            } finally {
                try {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (Exception e) {

                    Log.d("MainActivityFragment", e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipes> recipes) {
            super.onPostExecute(recipes);
            initViews();
        }
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), StepsActivity.class);
        intent.putExtra("item", clickedItemIndex);
        startActivity(intent);
    }


    private void downloadRecipes(){
        if(checkConnection()){
                new FetchRecipeTask().execute();
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the list state
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            BundlePortraitViewState = layoutManager.onSaveInstanceState();
            outState.putParcelable(PORTRAIT_VIEW_STATE, BundlePortraitViewState);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            BundleLandscapeViewState = layoutManager.onSaveInstanceState();
            outState.putParcelable(LANDSCAPE_VIEW_STATE, BundleLandscapeViewState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                BundlePortraitViewState = savedInstanceState.getParcelable(PORTRAIT_VIEW_STATE);

            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                BundleLandscapeViewState = savedInstanceState.getParcelable(LANDSCAPE_VIEW_STATE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BundlePortraitViewState != null) {
            if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
                layoutManager.onRestoreInstanceState(BundlePortraitViewState);
            }
            else if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                layoutManager.onRestoreInstanceState(BundleLandscapeViewState);
            }

        }
    }

}
