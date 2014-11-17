package com.sjsu.techknowgeek.snakeonaplane;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends Activity {

    public static final int GRID_SIZE = 16; //Length of each grid side, assumed > 8

    private GLSurfaceView mGLSurfaceView;
    private FrameLayout gameFrameView;

    private static int lives;
    private static int level;
    private static long speed; //number of milliseconds between updating model
    private static Timer timer;

    public static int score;
    public static ArrayList<GameObject> objects; //Very first object assumed to be snake

    private TextView livesValue;
    private TextView scoreValue;
    private TextView levelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        lives = 3;
        level = 0;
        speed = 3000;
        timer = new Timer();

        score = 0;
        initializeObjectArray();

        livesValue = (TextView) findViewById(R.id.game_livesValue);
        scoreValue = (TextView) findViewById(R.id.game_scoreValue);
        levelTextView = (TextView) findViewById(R.id.game_levelStr);

        gameFrameView = (FrameLayout)findViewById(R.id.game_gameFrame);

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.graphics_glsurfaceview1);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
            mGLSurfaceView.setRenderer(new SnakeRender());
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();

        updateValues();
        startTimer();
    }

    /**
     * Initializes the objects Array list
     */
    private void initializeObjectArray()
    {
        int halfPoint = (GRID_SIZE-1)/2;

        objects = new ArrayList<GameObject>();

        //Snake object always starts from center of left-hand wall, moving right
        objects.add(new GameObject(1, 1, 0, halfPoint));

        //perimeter walls, 4 less due to corner overlap and 2 less due to entry/exit points
        for(int i = 0; i < GRID_SIZE; i++)
        {
            objects.add(new GameObject(0,0,i,0));
            objects.add(new GameObject(0,0,i,GRID_SIZE-1));
            if(i != halfPoint && i != 0 && i != GRID_SIZE-1){ //Entry/exit & prevent overlapping corners
                objects.add(new GameObject(0,0,0,i));
                objects.add(new GameObject(0,0,GRID_SIZE-1, i));
            }
        }

        //? obstacle walls
        level = level%3;

        switch (level)
        {
            default: //0 or level given was negative
                objects.add(new GameObject(0,0,(GRID_SIZE-1)/2,(GRID_SIZE/2)-2));
                objects.add(new GameObject(0,0,(GRID_SIZE-1)/2,(GRID_SIZE-1)/2));
                objects.add(new GameObject(0,0,(GRID_SIZE-1)/2,(GRID_SIZE/2)));
                objects.add(new GameObject(0,0,(GRID_SIZE-1)/2,(GRID_SIZE/2)+1));
                break;
            case 1:
                for(int i = 2; i<GRID_SIZE-1; i++)
                {
                    objects.add(new GameObject(0,0,((GRID_SIZE-1)/2),i));
                }
                break;
            case 2:
                for(int i = 1; i<GRID_SIZE-1; i++)
                {
                    if(i != halfPoint)
                        objects.add(new GameObject(0,0,3*((GRID_SIZE-1)/4),i));
                }
                objects.add(new GameObject(0,0,((GRID_SIZE-1)/4),(GRID_SIZE-2)/2));
                objects.add(new GameObject(0,0,((GRID_SIZE-1)/4),(GRID_SIZE-1)/2));
                objects.add(new GameObject(0,0,((GRID_SIZE-1)/4),(GRID_SIZE/2)));
                objects.add(new GameObject(0,0,((GRID_SIZE-1)/4),(GRID_SIZE+1)/2));
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }

    private void startTimer()
    {
        TimerTask task = new TimerTask() {
            /**
             * The task to run should be specified in the implementation of the {@code run()}
             * method.
             */
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkSnake();
                    }
                });
            }
        };
        timer.schedule(task, speed);
    }

    private void checkSnake()
    {
        Iterator<GameObject> itr = objects.iterator();
        GameObject snake = itr.next();

        snake.move();

        int halfPoint = (GRID_SIZE-1)/2;

        if(snake.getX() >= GRID_SIZE-1 && snake.getY() == halfPoint) //Level Complete
        {
            speed -= speed/4;
            score++;
            level++;
            updateValues();
            initializeObjectArray();
        }

        else { //Check for Collision
            for (; itr.hasNext(); ) {
                if (snake.isCollision(itr.next())) {
                    if(lives>0) {
                        lives--;
                        updateValues();
                        snake.setX(0);
                        snake.setY(halfPoint);
                    }
                    else {
                        //TODO: GAME OVER
                        //IF SCORE > HIGH SCORE
                        //STORE HIGH SCORE
                        //DISPLAY GAME OVER SCREEN
                        this.finish();
                        return;
                    }
                }
            }
        }
        startTimer();
    }

    private void updateValues()
    {
        scoreValue.setText(score+"");
        livesValue.setText(lives+"");
        levelTextView.setText("Level: "+level);
    }

    public void turnLeft(View view)
    {
        objects.get(0).rotateLeft();
    }

    public void turnRight(View view)
    {
        objects.get(0).rotateRight();
    }
}
