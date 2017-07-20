package com.example.princess.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.model.Ingredients;

import java.util.ArrayList;

/**
 * Created by Princess on 7/14/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private final ArrayList<Ingredients> ingredients;

    public IngredientAdapter (ArrayList<Ingredients> ingredients) {

        this.ingredients = ingredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_item, parent, false);
        return new IngredientHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {

        if(!ingredients.isEmpty()){
            holder.ingredient.setText(ingredients.get(position).getIngredient());
            holder.measure.setText(ingredients.get(position).getMeasure());
            holder.quantity.setText(String.valueOf(ingredients.get(position).getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder {

        TextView ingredient;
        TextView measure;
        TextView quantity;

        public IngredientHolder(View itemView) {
            super(itemView);

            ingredient = (TextView) itemView.findViewById(R.id.ingredient);
            measure = (TextView) itemView.findViewById(R.id.measure);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
        }
    }
}
