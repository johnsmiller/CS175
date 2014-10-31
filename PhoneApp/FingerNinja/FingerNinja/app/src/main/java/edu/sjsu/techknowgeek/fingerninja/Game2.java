package edu.sjsu.techknowgeek.fingerninja;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;


public class Game2 extends Activity implements View.OnTouchListener{

    private static int screenWidth;
    private static int screenHeight;

    private int score;

    private static Button button;
    private static RelativeLayout relativeLayout;
    private static GestureOverlayView gestureOveriew;

    private static Timer timer;
    private static final long TIMER_DURATION = 20*1000; //Time in miliseconds that the game lasts


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        getActionBar().hide(); //hide the actionbar

        score = 0;

        button = (Button) findViewById(R.id.game2Button);
        relativeLayout = (RelativeLayout) findViewById(R.id.game2RelativeLayout);
        gestureOveriew = (GestureOverlayView) findViewById(R.id.gestureOverlayView2);

        gestureOveriew.setOnTouchListener(this);

        timer = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game2, menu);
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

        score++;

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
        NetworkManager.sendGameStats("Swipe_Around", score);
        //TODO: Display "game over" screen?

        this.finish();
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(checkBounds(event))
            buttonClicked(null);
        if(event.getAction() == MotionEvent.ACTION_UP){
            score = 0;
        }
        return true;
    }

    private boolean checkBounds(MotionEvent event)
    {
        float xLow = button.getX();
        float xHigh = button.getX() + button.getMeasuredWidth();
        float yLow = button.getY();
        float yHigh = button.getY() + button.getMeasuredHeight();

        float eventX = event.getX();
        float eventY = event.getY();

        return((eventX >= xLow && eventX <= xHigh) && (eventY >= yLow && eventY <= yHigh));
    }
}
