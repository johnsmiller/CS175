package com.sjsu.techknowgeek.snakeonaplane;
import android.database.sqlite.*;
import android.database.*;
import android.content.*;
import android.util.*;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {
    private int currentHighScore=0;
    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;
    public TextView highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();
        highScore = (TextView) this.findViewById(R.id.main_highScoreValue);
        dbOpenHelper = new DBOpenHelper(this, "My_Database", 3);
        db = dbOpenHelper.getWritableDatabase();

//insert into database a score of 5

        db.execSQL("INSERT OR REPLACE INTO SCORES VALUES (5)");
        db.close();

        dbOpenHelper = new DBOpenHelper(this, "My_Database", 3);

        db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM SCORES", null); //this iterates across the db

        //check for updated high score
        for(int i=0; i< c.getCount(); i++)
        {
            c.moveToNext();
            if(c.getInt(0)>currentHighScore)
                currentHighScore = c.getInt(0);
        }
        
        highScore.setText(" " + currentHighScore);
        db.close();
    }

//begin database code
    private static class DBOpenHelper extends SQLiteOpenHelper
    {
        public DBOpenHelper(Context context, String dbName, int version )
        {
            // third argument id to a cursor factory -- we don't care about
            super(context, dbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try
            {
                db.execSQL("CREATE TABLE IF NOT EXISTS SCORES(HIGHSCORE INTEGER PRIMARY KEY)");
            }
            catch(SQLException e)
            {
                Log.e("SqliteAndroid", "DBOpenHelper", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS SCORES");
            this.onCreate(db);
        }
    }
    //end database code

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startGame(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}


