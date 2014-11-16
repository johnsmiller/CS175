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
import android.widget.FrameLayout;

import java.util.ArrayList;


public class GameActivity extends Activity {

    public static final int GRID_SIZE = 16; //Length of each grid size, assumed > 8

    private GLSurfaceView mGLSurfaceView;
    private FrameLayout gameFrameView;

    private ArrayList<GameObject> objects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeObjectArray(1);

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
    }

    /**
     * Initializes the objects Array list
     * @param level valid range of 0 through 2
     */
    private void initializeObjectArray(int level)
    {
        int halfPoint = (GRID_SIZE-1)/2;
        int snakeSpeed = 1;
        if(objects != null)
            snakeSpeed += objects.get(0).getSpeed();

        objects = new ArrayList<GameObject>();

        //Snake object always starts from center of left-hand wall, moving right
        objects.add(new GameObject(1, snakeSpeed, 0, halfPoint));

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
}
