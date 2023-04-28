package com.example.practice5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.practice5.model.MapGuess;
import com.example.practice5.model.MapGuessAdapter;
import com.example.practice5.model.UserStats;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private UserStats userStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize user stats
        userStats = new UserStats();

        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.nav_game == item.getItemId()) {
                    // Go to the game fragment if the user selected the game option
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new GameFragment()).commit();
                    return true;
                }
                else if (R.id.nav_stats == item.getItemId()) {
                    // Go to the stats fragment if the user selected the stats option
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new StatsFragment()).commit();
                    return true;
                }
                else if (R.id.nav_help == item.getItemId()) {
                    // Go to the help fragment if the user selected the help option
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HelpFragment()).commit();
                    return true;
                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GameFragment()).commit();
    }

    public UserStats getUserStats() {
        return this.userStats;
    }
}