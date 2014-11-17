package com.sjsu.techknowgeek.snakeonaplane;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();

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
                        end();
                    }
                });
            }
        };
        Timer aTimer = new Timer();
        aTimer.schedule(task, 5000);
    }

    private void end() {
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_over, menu);
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
}
