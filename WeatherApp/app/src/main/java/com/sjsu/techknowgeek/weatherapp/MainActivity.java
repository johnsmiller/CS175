package com.sjsu.techknowgeek.weatherapp;

import android.database.sqlite.*;
import android.database.*;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, View.OnTouchListener{

    private LocationClient mLocationClient;
    private ProgressBar mActivityIndicator;

    protected static TextView mCityTextView;
    private static TextView mTemperatureTextView;
    private static GestureOverlayView mGestureView;

    public final String WEATHER_URL_HEAD = "http://www.worldweatheronline.com/v2/rss.ashx?q=";
    public final String XMLPREFIX = "Temp: </b>";
    public final String XMLDELIMIT = "&deg;c (";
    public final String XMLSUFFIX = "&deg;f";

    private static String mZipCode;
    private static String TempinC;
    private static String TempinF;

    private static boolean isF;

    private static SQLiteDatabase db;
    private static DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //hide action bar
        ActionBar bar = getActionBar();
        if(bar != null)
            bar.hide();

        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        //create database
        dbOpenHelper = new DBOpenHelper(this, "My_Database", 3);

        isF = true;

        mCityTextView = (TextView) findViewById(R.id.currentLocation);
        mTemperatureTextView = (TextView) findViewById(R.id.tempValue);
        mGestureView = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
        mLocationClient = new LocationClient(this, this, this);

        mGestureView.setOnTouchListener(this);

        //load previous location from SQL database here
        readFromDatabase();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event != null && event.getAction() == MotionEvent.ACTION_UP) {
            isF = !isF;
            setTemperature(null);
        }
        return true;
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

    //Weather update code

    public void startWeatherUpdate(View view)
    {
        mLocationClient.connect();
    }

    private void getWeather(String postalCode)
    {
        Toast.makeText(this, "Postal Code: " + postalCode, Toast.LENGTH_LONG).show();

        /* Download weather from internet */
        // Get URL for xml
        String url = WEATHER_URL_HEAD + postalCode;
        String[] temps = new String[2];
        // Download XML & Parse file
        try {
            temps = downloadUrl(url);
        } catch (IOException ioe) {
            Toast.makeText(this, "Death do us part", Toast.LENGTH_SHORT).show();
            ioe.printStackTrace();
        }

        TempinC = temps[0];
        TempinF = temps[1];

        // update text view with weather
        setTemperature(null);
    }

    public void setTemperature(View view)
    {
        if(TempinC == null) {
            Toast.makeText(this, "Please wait for temperature to update", Toast.LENGTH_LONG).show();
            return;
        }
        if(view != null)
            isF = !isF;
        if(isF)
        {
            mTemperatureTextView.setText(TempinF + "F");
        }
        else
        {
            mTemperatureTextView.setText(TempinC + "C");
        }
    }

    //Methods required by GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener interfaces
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

    //Location and weather background task
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
                        getWeather(address.getPostalCode());
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
        }
    }

    private String[] downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        int len = 2000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            int prefix = contentAsString.indexOf(XMLPREFIX);
            int delimit = contentAsString.indexOf(XMLDELIMIT);
            int suffix = contentAsString.indexOf(XMLSUFFIX);

            String cStr = contentAsString.substring(prefix + XMLPREFIX.length(), delimit);
            String fStr = contentAsString.substring(delimit + XMLDELIMIT.length(), suffix);

            String[] degArr = new String[2];
            degArr[0] = cStr;
            degArr[1] = fStr;

            return degArr;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
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
        try {
            if (c.getCount() > 0) {
                mZipCode = c.getString(c.getCount());
                mCityTextView.setText(mZipCode);
            }
        } catch (Exception ex)
        {

        }
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        startWeatherUpdate(null);
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        writeToDatabase();
        dbOpenHelper.close();
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
}
