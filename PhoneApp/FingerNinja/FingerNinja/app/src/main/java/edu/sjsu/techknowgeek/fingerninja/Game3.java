package edu.sjsu.techknowgeek.fingerninja;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.os.CountDownTimer;

public class Game3 extends Activity {

    private static int screenWidth;
    private static int screenHeight;

    private static Button button;
    private static RelativeLayout relativeLayout;
    private CountdownTimer countDownTimer;
    private static Timer timer;
    private static final long TIMER_DURATION = 20*1000; //Time in miliseconds that the game lasts
    private final long startTime = 20 * 1000;
    private final long interval = 1 * 1000;
    private int currentScore=0;
    public TextView score;
    public TextView timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);

        getActionBar().hide(); //hide the actionbar


        button = (Button) findViewById(R.id.game1Button);
        relativeLayout = (RelativeLayout) findViewById(R.id.game1RelativeLayout);
        score = (TextView) this.findViewById(R.id.score);
        timeRemaining = (TextView) this.findViewById(R.id.timeRemaining);
        timer = null;
        score.setText(" "+currentScore);
        countDownTimer = new TheAmountOfTimeRemaining(startTime, interval);

    }

    public class TheAmountOfTimeRemaining extends CountDownTimer {

        public TheAmountOfTimeRemaining(long startTime, long interval) {

            super(startTime, interval);

        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game1, menu);
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

    /**
     * Called when button is clicked. Randomizes button location
      * @param view
     */
    public void buttonClicked(View view)
    {
        if(timer == null) {
            createTimer();
        }

        //Update screenWidth/screenHeight
        screenWidth = relativeLayout.getMeasuredWidth();
        screenHeight = relativeLayout.getMeasuredHeight();
        currentScore++;
        score.setText(" " + currentScore);
        timeRemaining.setText(" "+timer.toString());
        //calculate new random position
            //heavier weights for upper 50% of screen
       // float widthRand = Math.round(Math.random()*(screenWidth-button.getMeasuredWidth()+1));
       // float heightRand = Math.round(Math.random() * (screenHeight - button.getMeasuredHeight() + 1));
        //if(heightRand > (.5 * screenHeight)) //get new random if near bottom
         //   heightRand = Math.round(Math.random()*(screenHeight-button.getMeasuredHeight()+1));

//        //move button to random location within screen bounds
//        button.setX(widthRand);
//        button.setY(heightRand);
    }

    private void createTimer()
    {
        TimerTask aTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameOver();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(aTask, TIMER_DURATION);
        countDownTimer.start();
    }

    private void gameOver()
    {
        //TODO: Upload results to server
        //TODO: Display "game over" screen?
        //reset score to 0
        currentScore = 0;
        this.finish();
    }
}
