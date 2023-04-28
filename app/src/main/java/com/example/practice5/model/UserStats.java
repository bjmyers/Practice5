package com.example.practice5.model;

public class UserStats {

    private int gamesStarted;
    private int gamesCompleted;
    private double averageNumGuesses;
    private Integer bestGame;
    private Integer worstGame;

    public UserStats() {
        // Initialization constructor
        this.gamesStarted = 0;
        this.gamesCompleted = 0;
        this.averageNumGuesses = 0.0;
        this.bestGame = null;
        this.worstGame = null;
    }

    public void gameStarted() {
        // The user started a new game
        this.gamesStarted++;
    }

    public void gameCompleted(final int numGuesses) {
        // The user completed a game in a given amount of steps, update their stats
        final double totalGuesses = this.averageNumGuesses * this.gamesCompleted + numGuesses;
        this.averageNumGuesses = totalGuesses / (this.gamesCompleted + 1);
        this.gamesCompleted++;
        if (this.bestGame == null || numGuesses < this.bestGame) {
            // New best game
            this.bestGame = numGuesses;
        }
        if (this.worstGame == null || numGuesses > this.worstGame) {
            // New worst game
            this.worstGame = numGuesses;
        }
    }

    public int getGamesStarted() {
        return gamesStarted;
    }

    public int getGamesCompleted() {
        return gamesCompleted;
    }

    public double getAverageNumGuesses() {
        return averageNumGuesses;
    }

    public Integer getBestGame() {
        return bestGame;
    }

    public Integer getWorstGame() {
        return worstGame;
    }
}
