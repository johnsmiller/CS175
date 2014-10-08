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

    private static long time;
    private static String playerName;
    private static int highScore;

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

        } else {
            // Probably should initialize members with default values for a new instance
            time = 1000; //
            playerName = "Player 1";
            highScore = 0;
        }

        setContentView(R.layout.activity_menuscreen);

        final View contentView = findViewById(R.id.fullscreen_content);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putLong(STATE_TIME, time);
        savedInstanceState.putString(STATE_PLAYER_NAME, playerName);
        savedInstanceState.putInt(STATE_HIGH_SCORE, highScore);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(long time) {
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
