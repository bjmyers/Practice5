package com.example.practice5.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice5.R;

import java.util.ArrayList;
import java.util.List;

public class MapGuessAdapter extends RecyclerView.Adapter<MapGuessAdapter.MapGuessHolder> {

    private List<MapGuess> guesses;

    public MapGuessAdapter() {
        guesses = new ArrayList<>();
    }

    @NonNull
    @Override
    public MapGuessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guess_item, parent, false);
        return new MapGuessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapGuessHolder holder, int position) {
        MapGuess guess = guesses.get(position);
        holder.bind(guess);
    }

    @Override
    public int getItemCount() {
        return guesses.size();
    }

    public void addGuess(MapGuess guess) {
        guesses.add(guess);
        notifyItemInserted(guesses.size());
        notifyDataSetChanged();
    }

    static class MapGuessHolder extends RecyclerView.ViewHolder {

        private TextView mGuessNumberView;
        private TextView mPercentReducedView;

        public MapGuessHolder(@NonNull View itemView) {
            super(itemView);
            mGuessNumberView = itemView.findViewById(R.id.guess_number);
            mPercentReducedView = itemView.findViewById(R.id.percent_map_reduced);
        }

        private void bind(MapGuess guess) {
            mGuessNumberView.setText(String.valueOf(guess.getGuessNumber()));
            mPercentReducedView.setText(String.format("%.2f", guess.getMapFractionReduced() * 100) + "%");
        }
    }
}
