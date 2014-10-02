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


/**
 *
 *
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_settings);

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

    public void sendMainMenuActivity()
    {
        //Main Menu initiated this activity, therefore it only needs to close to return to the menu
        //Intent intent = new Intent(this, menuscreenActivity.class);
        //startActivity(intent);
        this.finish();
    }
}
