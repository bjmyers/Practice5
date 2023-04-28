package com.example.practice5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.practice5.model.UserStats;

public class StatsFragment extends Fragment {

    private TextView mGamesStarted;
    private TextView mGamesCompleted;
    private TextView mAvgGuesses;
    private TextView mBestGame;
    private TextView mWorstGame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_stats, container, false);

        // Get user stats from main activity
        final UserStats userStats = ((MainActivity) getActivity()).getUserStats();

        mGamesStarted = view.findViewById(R.id.stat_game_started);
        mGamesCompleted = view.findViewById(R.id.stat_game_completed);
        mAvgGuesses = view.findViewById(R.id.stat_avg_guesses);
        mBestGame = view.findViewById(R.id.stat_best_game);
        mWorstGame = view.findViewById(R.id.stat_worst_game);

        // Update text views based on user stats
        mGamesStarted.setText(String.valueOf(userStats.getGamesStarted()));
        mGamesCompleted.setText(String.valueOf(userStats.getGamesCompleted()));
        mAvgGuesses.setText(String.valueOf(userStats.getAverageNumGuesses()));
        if (userStats.getBestGame() == null) {
            mBestGame.setText("N/a");
        }
        else {
            mBestGame.setText(String.valueOf(userStats.getBestGame()));
        }
        if (userStats.getWorstGame() == null) {
            mWorstGame.setText("N/a");
        }
        else {
            mWorstGame.setText(String.valueOf(userStats.getWorstGame()));
        }

        return view;
    }
}
