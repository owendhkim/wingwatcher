package com.example.wingwatcher;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wingwatcher.Bird;
import com.example.wingwatcher.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BirdAdapter extends RecyclerView.Adapter<BirdAdapter.BirdViewHolder> implements Filterable {
    private List<Bird> birdList;
    private List<Bird> birdListFull;
    private Context context; // Add a context variable

    public BirdAdapter(List<Bird> birdList, Context context) {
        this.birdList = birdList;
        birdListFull = new ArrayList<>(birdList);
        this.context = context; // Initialize the context
    }

    @NonNull
    @Override
    public BirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cards_page, parent, false);
        return new BirdViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BirdViewHolder holder, int position) {
        Bird bird = birdList.get(position);
        holder.birdNameTextView.setText(bird.getName());

        // Load bird image using Picasso
        Picasso.get().load(bird.getImageUrl()).into(holder.birdImageView);


        holder.learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int birdId = bird.getId(); // Assuming you have a getId() method in your Bird class
                openBirdInfoActivity(birdId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return birdList.size();
    }

    public void filterList(List<Bird> filteredList) {
        birdList = filteredList;
        notifyDataSetChanged();
    }

    static class BirdViewHolder extends RecyclerView.ViewHolder {
        ImageView birdImageView;
        TextView birdNameTextView;
        Button learnMoreButton; // Add the "Learn More" button

        BirdViewHolder(View itemView) {
            super(itemView);
            birdImageView = itemView.findViewById(R.id.birdImage);
            birdNameTextView = itemView.findViewById(R.id.birdName);
            learnMoreButton = itemView.findViewById(R.id.learnMoreButton); // Initialize the button
        }
    }

    private void openBirdInfoActivity(int birdId) {
        Intent intent = new Intent(context, birdInfo.class);
        intent.putExtra("birdID", birdId);
        context.startActivity(intent);
    }

    @Override
    public Filter getFilter() {
        return birdFilter;
    }

    private Filter birdFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Bird> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(birdListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Bird bird : birdListFull) {
                    if (bird.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(bird);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            birdList.clear();
            birdList.addAll((List<Bird>) results.values);
            notifyDataSetChanged();
        }
    };
}
