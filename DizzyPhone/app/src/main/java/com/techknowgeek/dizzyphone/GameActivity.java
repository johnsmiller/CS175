package com.techknowgeek.dizzyphone;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends Activity {

    private static TextView highScoreView;
    private static TextView playerScoreView;
    private static TextView livesView;

    private long time;
    private String playerName;
    private int highScore;
    private int playerScore;
    private int lives;

    private Timer zehTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // Create TextView Classes
        highScoreView = (TextView)findViewById(R.id.currentHighScore);
        playerScoreView = (TextView)findViewById(R.id.currentScore);
        livesView = (TextView)findViewById(R.id.currentLives);
        // Assign Vars and Get Highscore
        highScore = menuscreenActivity.getHighScore();
        time = menuscreenActivity.getTime();

        TimerTask aTask = new TimerTask() {
            @Override
            public void run(){
                upDateTextViews();
                timeIsUp();
            }
        };
        zehTimer = new Timer();
        zehTimer.scheduleAtFixedRate(aTask, time, time);
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

    public void onButtonClick(){
        // Kill timer
        playerScore++;
        // Update Scores Views
        playerScoreView.setText(String.valueOf(playerScore));
    }

    private void timeIsUp(){
        // Flash background color to notify user.
        // Its not required but might make the app easier

        if(lives <= 0) {
            // IF playerScore > highScore THEN update highScore to playerScore in menuscreen
            // GAME OVER and display score before going back to main menu
            this.finish();
        } else {
            lives--;
            // Update Scores Views
        }
    }

    public void upDateTextViews(){
        highScoreView.setText(String.valueOf(highScore));
        playerScoreView.setText(String.valueOf(playerScore));
        livesView.setText(String.valueOf(lives));
        //Updated textviews in here
    }

    /* public int getScreenOrientation()
{
    Display getOrient = getWindowManager().getDefaultDisplay();
    int orientation = Configuration.ORIENTATION_UNDEFINED;
    if(getOrient.getWidth()==getOrient.getHeight()){
        orientation = Configuration.ORIENTATION_SQUARE;
    } else{
        if(getOrient.getWidth() < getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_PORTRAIT;
        }else {
             orientation = Configuration.ORIENTATION_LANDSCAPE;
        }
    }
    return orientation;
} */
}
