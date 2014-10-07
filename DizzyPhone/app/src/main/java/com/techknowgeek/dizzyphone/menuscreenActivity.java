package com.techknowgeek.dizzyphone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 *
 *
 */
public class menuscreenActivity extends Activity {

    static final String STATE_TIME = "time";
    static final String STATE_PLAYER_NAME = "playerName";
    static final String STATE_HIGH_SCORE = "highScore";
    static final String STATE_SCORE = "playerScore";
    static final String STATE_LIVES = "lives";

    private static double time;
    private static String playerName;
    private static int highScore;
    private static int playerScore;
    private static int lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            time = savedInstanceState.getInt(STATE_TIME);
            playerName = savedInstanceState.getString(STATE_PLAYER_NAME);
            highScore = savedInstanceState.getInt(STATE_HIGH_SCORE);
            playerScore = savedInstanceState.getInt(STATE_SCORE);

        } else {
            // Probably should initialize members with default values for a new instance
            time = 1.0;
            playerName = "Player 1";
            highScore = 0;
            playerScore = 0;
            lives = 3; // Start with three lives
        }

        setContentView(R.layout.activity_menuscreen);

        final View contentView = findViewById(R.id.fullscreen_content);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putDouble(STATE_TIME, time);
        savedInstanceState.putString(STATE_PLAYER_NAME, playerName);
        savedInstanceState.putInt(STATE_HIGH_SCORE, highScore);
        savedInstanceState.putInt(STATE_SCORE, playerScore);
        savedInstanceState.putInt(STATE_LIVES, lives);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public static double getTime() {
        return time;
    }

    public static void setTime(double time) {
        menuscreenActivity.time = time;
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        menuscreenActivity.playerName = playerName;
    }

    public static int getHighScore() {
        return highScore;
    }

    public static void setHighScore(int highScore) {
        menuscreenActivity.highScore = highScore;
    }

    public static int getPlayerScore() {
        return playerScore;
    }

    public static void setPlayerScore(int playerScore) {
        menuscreenActivity.playerScore = playerScore;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        menuscreenActivity.lives = lives;
    }

    public void sendGameActivity(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void sendSettingsActivity(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
