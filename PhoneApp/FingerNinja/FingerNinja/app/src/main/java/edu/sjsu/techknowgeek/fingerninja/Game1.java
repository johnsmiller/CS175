package edu.sjsu.techknowgeek.fingerninja;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;


public class Game1 extends Activity {

    private static int screenWidth;
    private static int screenHeight;

    private int score;

    private static Button button;
    private static RelativeLayout relativeLayout;

    private static Timer timer;
    private static final long TIMER_DURATION = 20*1000; //Time in miliseconds that the game lasts


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        getActionBar().hide(); //hide the actionbar

        score = 0;

        button = (Button) findViewById(R.id.game1Button);
        relativeLayout = (RelativeLayout) findViewById(R.id.game1RelativeLayout);

        timer = null;
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

        //calculate new random position
            //heavier weights for upper 50% of screen
        float widthRand = Math.round(Math.random()*(screenWidth-button.getMeasuredWidth()+1));
        float heightRand = Math.round(Math.random() * (screenHeight - button.getMeasuredHeight() + 1));
        if(heightRand > (.5 * screenHeight)) //get new random if near bottom
            heightRand = Math.round(Math.random()*(screenHeight-button.getMeasuredHeight()+1));

        //move button to random location within screen bounds
        button.setX(widthRand);
        button.setY(heightRand);

        score++;
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
    }

    private void gameOver()
    {
        NetworkManager.sendGameStats("Hungry_Frog", score);
        //TODO: Display "game over" screen?

        this.finish();
    }
}
