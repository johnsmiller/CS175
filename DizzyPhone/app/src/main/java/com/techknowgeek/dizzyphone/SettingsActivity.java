package com.techknowgeek.dizzyphone;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 *
 *
 */
public class SettingsActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    private EditText mPlayerName;

    public final int minimumSliderValue = 1;
    public final int maxSliderValue = 20;
    public final int sliderValueDivisor = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mPlayerName = (EditText) findViewById(R.id.editText);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setMax(maxSliderValue-minimumSliderValue);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setProgress(Math.round(Math.round((menuscreenActivity.getTime()-100)/100)));
        setSliderView();

        if(menuscreenActivity.getPlayerName() != null)
            mPlayerName.setText(menuscreenActivity.getPlayerName());



        final View contentView = findViewById(R.id.fullscreen_content);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSaveDialog(View view)
    {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        SaveSettingsDialogFragment newFragment = new SaveSettingsDialogFragment();
        newFragment.setCaller(this);
        newFragment.show(ft, "dialog");
    }

    public void saveSettings()
    {
        //Save Name
        EditText textView = (EditText)findViewById(R.id.editText);
        menuscreenActivity.setPlayerName(textView.getText().toString());
        //Save Time Delay
        menuscreenActivity.setTime(setSliderView());
    }

    public void sendMainMenuActivity()
    {
        //Main Menu initiated this activity, therefore it only needs to close to return to the menu
        //Intent intent = new Intent(this, menuscreenActivity.class);
        //startActivity(intent);
        this.finish();
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max
     *                 was set by {}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setSliderView();
    }

    private long setSliderView()
    {
        int progress = mSeekBar.getProgress();
        TextView textView = (TextView)findViewById(R.id.seekbar_value);
        textView.setText((((progress+minimumSliderValue)/(double)sliderValueDivisor)+"").substring(0, 3) + " Seconds Delay");
        return ((progress + minimumSliderValue)*(1000/sliderValueDivisor));
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
