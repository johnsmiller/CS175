package com.techknowgeek.dizzyphone;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends Activity {

    private static TextView highScoreView;
    private static TextView playerScoreView;
    private static TextView livesView;
    private static TextView currentOrientation;
    private static TextView fullScreenView;

    private long time;
    private String playerName;
    private static int highScore;
    private static int playerScore;
    private static int lives;

    private static boolean isPortrait; //True if requested orientation is portrait

    private Timer zehTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //WILL BE CALLED ON ORIENTATION CHANGE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); //Must be called BEFORE getting ViewByID

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // Create TextView Classes
        highScoreView = (TextView)findViewById(R.id.currentHighScore);
        playerScoreView = (TextView)findViewById(R.id.currentScore);
        livesView = (TextView)findViewById(R.id.currentLives);
        currentOrientation = (TextView) findViewById(R.id.currentOrientation);
        fullScreenView = (TextView) findViewById(R.id.fullscreen_content);

        // Assign Vars and Get Highscore
        highScore = menuscreenActivity.getHighScore();
        time = menuscreenActivity.getTime();
        playerScore = 0;
        lives = 3;

        upDateTextViews();
        randomOrientation();
        setTimerTask();
    }

    private void setTimerTask()
    {
        TimerTask aTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        upDateTextViews();
                        timeIsUp();
                    }
                });
            }
        };
        zehTimer = new Timer();
        fullScreenView.setBackgroundColor(0x0099cc);
        zehTimer.schedule(aTask, time);
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

    public void onButtonClick(View view){
        if(isCorrectOrientation()) {
            zehTimer.cancel();
            playerScore++;
            upDateTextViews();
            randomOrientation();
            setTimerTask();
        }
    }

    private void randomOrientation()
    {
        if(Math.random() >= .5)
        {
            //portrait
            isPortrait=true;
            currentOrientation.setText("Portrait");
        }
        else {
            //landscape
            isPortrait=false;
            currentOrientation.setText("Landscape");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isCorrectOrientation()
    {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){ //Portrait
            return isPortrait;
        }else { //Landscape
            return !isPortrait;
        }

    }

    private void timeIsUp(){
        // Flash background color to notify user.
        // Its not required but might make the app easier
        fullScreenView.setBackgroundColor(Color.RED);

        if(lives <= 0) {
            // IF playerScore > highScore THEN update highScore to playerScore in menuscreen
            // GAME OVER and display score before going back to main menu
            if(playerScore > menuscreenActivity.getHighScore())
                menuscreenActivity.setHighScore(playerScore);
            Intent intent = new Intent(this, gameover.class);
            startActivity(intent);
            this.finish();
        } else {
            lives--;
            randomOrientation();
            upDateTextViews();
            setTimerTask();
            // Update Scores Views
        }
    }

    public static void upDateTextViews(){
        highScoreView.setText(String.valueOf(highScore));
        playerScoreView.setText(String.valueOf(playerScore));
        livesView.setText(String.valueOf(lives));
        //Updated textviews in here
    }

    public static int getPlayerScore()
    {
        return playerScore;
    }

}
