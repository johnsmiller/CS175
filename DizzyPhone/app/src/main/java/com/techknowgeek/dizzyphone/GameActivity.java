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
    private static int highScore;
    private static int playerScore;
    private static int lives;

    private Timer zehTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // Create TextView Classes
        highScoreView = (TextView)findViewById(R.id.currentHighScore);
        playerScoreView = (TextView)findViewById(R.id.currentScore);
        livesView = (TextView)findViewById(R.id.currentLives);
        System.out.println("Values Assigned");
        nullCheck();
        // Assign Vars and Get Highscore
        highScore = menuscreenActivity.getHighScore();
        time = menuscreenActivity.getTime();
        playerScore = 0;
        lives = 3;

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
            setTimerTask();
            // Update Scores Views
        }
    }

    public static void upDateTextViews(){
        try {
            highScoreView.setText(String.valueOf(highScore));
            playerScoreView.setText(String.valueOf(playerScore));
            livesView.setText(String.valueOf(lives));
            //Updated textviews in here
        } catch (NullPointerException ex) {
            System.out.println("\n\n\n\n\n\nNULL EXCEPTION!!!!!!!!!!\n\n\n\n\n\n\n");
            nullCheck();
        }
    }

    private static void nullCheck()
    {
        if(highScoreView == null)
        {
            System.out.println("highScoreView is null");
        }
        if(playerScoreView == null)
        {
            System.out.println("playerScoreView is null");
        }
        if(livesView == null)
        {
            System.out.println("livesView is null");
        }
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
