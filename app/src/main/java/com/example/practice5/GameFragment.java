package com.example.practice5;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice5.model.MapGuess;
import com.example.practice5.model.MapGuessAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Random;

public class GameFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private RecyclerView mPreviousGuesses;
    private MapGuessAdapter adapter;
    private Polygon currentPolygon;
    private int numGuesses;
    private double minLat;
    private double maxLat;
    private double minLong;
    private double maxLong;
    private double targetLat;
    private double targetLong;
    private boolean gameCompleted;
    private FloatingActionButton floatingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Initialize view
        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        floatingButton = view.findViewById(R.id.new_game_button);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

        mPreviousGuesses = view.findViewById(R.id.previous_guesses);
        mPreviousGuesses.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MapGuessAdapter();
        mPreviousGuesses.setAdapter(adapter);

        numGuesses = 0;
        // Initial box covering Pennsylvania
        minLat = 39.7;
        maxLat = 42;
        minLong = -80.5;
        maxLong = -75;
        currentPolygon = null;
        gameCompleted = false;

        Random rand = new Random();

        targetLat = rand.nextDouble() * (maxLat - minLat) + minLat;
        targetLong = rand.nextDouble() * (maxLong - minLong) + minLong;

        Log.i("GameFragment", "Target Latitude: " + targetLat);
        Log.i("GameFragment", "Target Longitude: " + targetLong);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        return view;
    }

    public void restartGame() {
        // Remove old marker
        googleMap.clear();

        // Reset the recycler view
        adapter = new MapGuessAdapter();
        mPreviousGuesses.setAdapter(adapter);

        // Reset all the initial values
        numGuesses = 0;
        minLat = 39.7;
        maxLat = 42;
        minLong = -80.5;
        maxLong = -75;
        currentPolygon = null;
        gameCompleted = false;
        Random rand = new Random();
        targetLat = rand.nextDouble() * (maxLat - minLat) + minLat;
        targetLong = rand.nextDouble() * (maxLong - minLong) + minLong;

        // Re-draw the polygon and re-center the map
        drawPolygonOnMap();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng((maxLat + minLat) / 2.0, (maxLong + minLong) / 2.0),
                6));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;

        drawPolygonOnMap();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng((maxLat + minLat) / 2.0, (maxLong + minLong) / 2.0),
                6));

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (gameCompleted) {
                    // Don't do anything if the game is already done
                    return;
                }
                numGuesses++;

                if (numGuesses == 1) {
                    // Only count the game as "started" when they make their first guess
                    ((MainActivity) getActivity()).getUserStats().gameStarted();
                }

                if (getHaversineDistance(new LatLng(targetLat, targetLong), latLng) < 8) {
                    MapGuess guess = new MapGuess(numGuesses, 1);
                    adapter.addGuess(guess);
                    finishGame();
                    return;
                }
                double latReduction;
                if (latLng.latitude < targetLat) {
                    // User picked below target latitude
                    latReduction = (maxLat - latLng.latitude) / (maxLat - minLat);
                    minLat = latLng.latitude;
                }
                else {
                    latReduction = (latLng.latitude - minLat) / (maxLat - minLat);
                    maxLat = latLng.latitude;
                }
                double longReduction;
                if (latLng.longitude < targetLong) {
                    // User picked left of target location
                    longReduction = (maxLong - latLng.longitude) / (maxLong - minLong);
                    minLong = latLng.longitude;
                }
                else {
                    longReduction = (latLng.longitude - minLong) / (maxLong - minLong);
                    maxLong = latLng.longitude;
                }
                MapGuess guess = new MapGuess(numGuesses,
                        1 - latReduction * longReduction);
                adapter.addGuess(guess);
                drawPolygonOnMap();
            }
        });
    }

    private void drawPolygonOnMap() {
        if (currentPolygon != null) {
            //This is not the first time drawing the polygon, clear the old one
            currentPolygon.remove();
        }

        LatLng topLeft = new LatLng(maxLat, minLong);
        LatLng topRight = new LatLng(maxLat, maxLong);
        LatLng bottomRight = new LatLng(minLat, maxLong);
        LatLng bottomLeft = new LatLng(minLat, minLong);

        currentPolygon = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(topLeft, topRight, bottomRight, bottomLeft));
    }

    private void finishGame() {
        currentPolygon.remove();
        gameCompleted = true;

        // Show them the secret location
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(targetLat, targetLong))
                .title("The secret location"));

        // Update user's stats
        ((MainActivity) getActivity()).getUserStats().gameCompleted(this.numGuesses);

        Toast.makeText(getContext(), "You won!", Toast.LENGTH_SHORT).show();
    }

    private double getHaversineDistance(final LatLng posn1, final LatLng posn2) {
        // Return distance in miles between two LatLngs
        // ref: https://cloud.google.com/blog/products/maps-platform/how-calculate-distances-map-maps-javascript-api

        final double rEarth = 3558.8; // radius of Earth in miles
        final double lat1 = posn1.latitude * Math.PI / 180.0;
        final double lat2 = posn2.latitude * Math.PI / 180.0;
        final double latDiff = lat2 - lat1;
        final double long1 = posn1.longitude * Math.PI / 180.0;
        final double long2 = posn2.longitude * Math.PI / 180.0;
        final double longDiff = long2 - long1;
        return 2 * rEarth * Math.asin(Math.sqrt(Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.sin(longDiff / 2) * Math.sin(longDiff / 2)));
    }
}
