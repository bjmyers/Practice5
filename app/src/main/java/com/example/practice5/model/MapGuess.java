package com.example.practice5.model;

public class MapGuess {

    private int guessNumber;
    private double mapFractionReduced;

    public MapGuess(int guessNumber, double mapPercentReduced) {
        this.guessNumber = guessNumber;
        this.mapFractionReduced = mapPercentReduced;
    }

    public int getGuessNumber() {
        return guessNumber;
    }

    public double getMapFractionReduced() {
        return mapFractionReduced;
    }
}
