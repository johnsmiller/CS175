package com.techknowgeek.dizzyphone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


/**
 *
 *
 */
public class menuscreenActivity extends Activity {

    public static final String PREFS_NAME ="DizzyPhonePreferences";

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

        // Restore/create instances
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        time = settings.getLong(STATE_TIME, 1000);
        playerName = settings.getString(STATE_PLAYER_NAME, "Player 1");
        highScore = settings.getInt(STATE_HIGH_SCORE, 0);

        setContentView(R.layout.activity_menuscreen);

        final View contentView = findViewById(R.id.fullscreen_content);

    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putLong(STATE_TIME, time);
        editor.putString(STATE_PLAYER_NAME, playerName);
        editor.putInt(STATE_HIGH_SCORE, highScore);

        // Commit the edits!
        editor.commit();
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
