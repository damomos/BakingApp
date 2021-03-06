package com.example.princess.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.model.Steps;

import java.util.ArrayList;

/**
 * Created by Princess on 7/14/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private Context context;
    private final ListItemClickListener listItemClickListener;
    private final ArrayList<Steps> steps;

    public StepAdapter(ListItemClickListener listener, ArrayList<Steps> steps) {
        listItemClickListener = listener;
        this.steps = steps;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

        holder.shortDescription.setText(steps.get(position).getShortDescription());
        holder.fullDescription.setText(steps.get(position).getFullDescription());

        String thumbnailUrl = steps.get(position).getThumbnailURL();
        context = holder.stepImage.getContext();
        if(!thumbnailUrl.isEmpty()){
            Glide.with(context).load(thumbnailUrl).into(holder.stepImage);
        } else {
            holder.stepImage.setImageResource(R.drawable.img_no_thumb);
        }

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shortDescription;
        TextView fullDescription;
        ImageView stepImage;

        public StepViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            shortDescription = (TextView) itemView.findViewById(R.id.short_desc);
            fullDescription = (TextView) itemView.findViewById(R.id.full_desc);
            stepImage = (ImageView) itemView.findViewById(R.id.step_image);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }
}
