package com.techknowgeek.dizzyphone;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends Activity {

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
            lives = 0;
        }

        setContentView(R.layout.activity_game);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public static void setStateTime(double timeState)
    {
        time = timeState;
    }

    public static void setStatePlayerName(String nameState)
    {
        playerName = nameState;
    }

    private static void setStateScore(int scoreState)
    {
        playerScore = scoreState;
    }

    private static void setStateHighScore(int scoreHighState)
    {
        highScore = scoreHighState;
    }

    private static void setStateLives(int livesState)
    {
        lives = livesState;
    }
}
