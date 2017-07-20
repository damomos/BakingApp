package com.example.princess.bakingapp.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.activities.StepsDetailActivity;
import com.example.princess.bakingapp.adapter.IngredientAdapter;
import com.example.princess.bakingapp.adapter.StepAdapter;
import com.example.princess.bakingapp.model.Steps;

import java.util.ArrayList;

import static com.example.princess.bakingapp.activities.MainActivity.isTablet;
import static com.example.princess.bakingapp.fragments.MainActivityFragment.bakes;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepsActivityFragment extends Fragment implements StepAdapter.ListItemClickListener{

    private RecyclerView stepsRecyclerView;
    private RecyclerView ingredientsRecyclerView;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    public static ArrayList<Steps> steps = new ArrayList<>();

    private int index = 100;

    public StepsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_steps, container, false);

        //Casting each view to RecyclerView
        stepsRecyclerView = (RecyclerView) view.findViewById(R.id.steps_list);
        ingredientsRecyclerView = (RecyclerView) view.findViewById(R.id.ingredients_list);

        //Setting the Layout
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Parsing the Intent
        index = getActivity().getIntent().getExtras().getInt("item");
        steps = bakes.get(index).getSteps();
        getActivity().setTitle(bakes.get(index).getName());

        //Setting the Adapters
        stepAdapter = new StepAdapter(this, steps);
        ingredientAdapter = new IngredientAdapter(bakes.get(index).getIngredients());

        //Setting the Recyclerview views
        stepsRecyclerView.setAdapter(stepAdapter);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        if(!isTablet) {
            Intent intent = new Intent(getActivity(), StepsDetailActivity.class);
            intent.putExtra("item", clickedItemIndex);
            startActivity(intent);

        }else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            StepsDetailActivityFragment stepsDetailActivityFragment = new StepsDetailActivityFragment();
            stepsDetailActivityFragment.index = clickedItemIndex;
            fragmentManager.beginTransaction()
                    .replace(R.id.stepsdetailsframe, stepsDetailActivityFragment)
                    .commit();
        }
    }
}
