package com.example.princess.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Princess on 7/14/2017.
 */

public class Recipes implements Parcelable{

    int id;
    private String name;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Steps> steps;
    private String servings;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }

    public Recipes (JSONObject recipe) {
        try {
            this.name = recipe.getString("name");
            this.ingredients = new ArrayList<>();
            JSONArray ingredient = recipe.getJSONArray("ingredients");
            for (int i = 0; i < ingredient.length(); i++) {
                ingredients.add(new Ingredients (ingredient.getJSONObject(i)));
            }
            this.steps = new ArrayList<>();
            JSONArray step = recipe.getJSONArray("steps");
            for (int i = 0; i < step.length(); i++) {
                steps.add(new Steps(step.getJSONObject(i)));
            }
            this.servings = recipe.getString("servings");
            this.image = recipe.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Recipes(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        servings = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeString(servings);
        dest.writeString(image);
    }
}
