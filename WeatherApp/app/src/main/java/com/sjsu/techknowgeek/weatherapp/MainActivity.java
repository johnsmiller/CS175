package com.sjsu.techknowgeek.weatherapp;

import android.database.sqlite.*;
import android.database.*;
import android.content.*;
import android.util.*;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{

    private LocationClient mLocationClient;
    private ProgressBar mActivityIndicator;

    private TextView mCityTextView;
    private TextView mTemperatureTextView;

    private static String mZipCode;

    private static SQLiteDatabase db;
    private static DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide action bar
        ActionBar bar = getActionBar();
        if(bar != null)
            bar.hide();

        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        //create database
        dbOpenHelper = new DBOpenHelper(this, "My_Database", 3);

        //start location update
        mLocationClient = new LocationClient(this, this, this);
        mCityTextView = (TextView) findViewById(R.id.currentLocation);
        mTemperatureTextView = (TextView) findViewById(R.id.tempValue);

        //load previous location from SQL database here
        //TODO: Load previous location

        //TODO: We need a GUI component that makes use of touch events (Refresh location button?? C/F degrees? Refresh Weather?)
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
                db.execSQL("CREATE TABLE IF NOT EXISTS LOCATION(LASTLOCATION CHAR(5) PRIMARY KEY)");
            }
            catch(SQLException e)
            {
                Log.e("SqliteAndroid", "DBOpenHelper", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS LOCATION");
            this.onCreate(db);
        }
    }


    //end database code

    private void getWeather(String postalCode)
    {
        //Just to show that it works for now
        Toast.makeText(this, "Postal Code: " + postalCode, Toast.LENGTH_LONG).show();
        //Download weather from internet
        //parse file
        //update text view with weather
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

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

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Location Connected. Updating Weather Info.", Toast.LENGTH_SHORT).show();
        Location mCurrentLocation = mLocationClient.getLastLocation();
        getAddress(mCurrentLocation);

    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected from location. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Location connection has failed :(", Toast.LENGTH_SHORT).show();
    }

    public void getAddress(Location mLocation) {
        // Ensure that a Geocoder services is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
            // Show the activity indicator
            mActivityIndicator.setVisibility(View.VISIBLE);
            (new GetAddressTask(this)).execute(mLocation);
        }
    }



    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;
        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e1) {
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                return "Bad Arguments passed to address service";
            }

            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                final Address address = addresses.get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCityTextView.setText(address.getLocality());
                    }
                });
                return address.getPostalCode();
            } else {
                return "No address found";
            }
        }

        @Override
        protected void onPostExecute(String postalCode) {
            mActivityIndicator.setVisibility(View.GONE);
            getWeather(postalCode);
        }
    }

    private static void writeToDatabase()
    {
        db = dbOpenHelper.getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO LOCATION VALUES (" + mZipCode + ")");
        db.close();
    }

    private static void readFromDatabase()
    {
        db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LOCATION", null); //this iterates across the db
        
        mZipCode = c.getString(c.getCount());
        db.close();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        dbOpenHelper.close();
    }

}
