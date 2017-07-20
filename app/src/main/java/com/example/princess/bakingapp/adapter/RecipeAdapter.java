package com.example.princess.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.model.Recipes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Princess on 7/14/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private final ListItemClickListener listItemClickListener;
    private final ArrayList<Recipes> recipes;

    public RecipeAdapter(ListItemClickListener listItemClickListener, ArrayList<Recipes> recipes) {
        this.listItemClickListener = listItemClickListener;
        this.recipes = recipes;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, int position){

        Recipes recipes_item = recipes.get(position);
        String serving = recipes_item.getServings();
        holder.name.setText(recipes_item.getName());
        holder.servings.setText("Servings: " + serving);

        String imageUrl = recipes_item.getImage();
        if(imageUrl.isEmpty()){
            if(recipes_item.getName().equals("Nutella Pie")){
                holder.imageView.setImageResource(R.drawable.nutella_pie);
            }else if(recipes_item.getName().equals("Brownies")){
                holder.imageView.setImageResource(R.drawable.brownies);
            }else if(recipes_item.getName().equals("Yellow Cake")){
                holder.imageView.setImageResource(R.drawable.yellow_cake);
            }else if(recipes_item.getName().equals("Cheesecake")) {
                holder.imageView.setImageResource(R.drawable.cheesecake);

        } else {
                Picasso.with(context).load(imageUrl).into(holder.imageView);
        }
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView name;
        TextView servings;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.recipe_image);
            name = (TextView) itemView.findViewById(R.id.recipe_name);
            servings = (TextView) itemView.findViewById(R.id.recipe_servings);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }
}
