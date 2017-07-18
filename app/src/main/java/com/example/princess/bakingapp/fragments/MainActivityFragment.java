package com.example.princess.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class MainActivityFragment extends Fragment implements RecipeAdapter.ListItemClickListener{

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    public static ArrayList<Recipes> bakes = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        loadRecipes();

        return view;
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void initViews(ArrayList<Recipes> bakes){
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recipe_list);
        if(isTablet){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {

            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }else {

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
        }
        recipeAdapter = new RecipeAdapter(this, bakes);
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), StepsActivity.class);
        intent.putExtra("item", clickedItemIndex);
        startActivity(intent);
    }

    public class FetchRecipe extends AsyncTask<Void, Void, ArrayList<Recipes>> {

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

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
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
            initViews(recipes);
        }
    }

    private void loadRecipes(){
        if(checkConnection()){
            new FetchRecipe().execute();
        }
    }


}
