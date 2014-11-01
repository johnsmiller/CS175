package edu.sjsu.techknowgeek.fingerninja;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class StatsFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static final String TAB_TITLE = "Statistics";

    private static View rootView;
    private final int[] IDS = {
        R.id.g1_stats,
        R.id.g2_stats,
        R.id.g3_stats
    };
    private ArrayList<TextView> scores;

    public static NetworkManager netManager;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatsFragment newInstance(int sectionNumber) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public StatsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        scores = new ArrayList<TextView>();

        for(int i = 0; i < IDS.length; i++){
            TableLayout table = (TableLayout) rootView.findViewById(IDS[i]);
            for(int j = 0; j < NetworkManager.SCORE_CATEGORIES.length; j++){
                scores.add((TextView) ((TableRow)table.getChildAt(j)).getChildAt(1));
            }
        }

        //updateStats();
        return rootView;
    }

    public void updateStats() {
        String[] scorestxt = NetworkManager.getGameStats();

        if(scorestxt.length == 18) {
            for(int i = 0; i < scorestxt.length; i++) {
                scores.get(i).setText(scorestxt[i]);
            }
        } else {
            Toast.makeText(rootView.getContext(), "Stats could not be parsed", Toast.LENGTH_LONG).show();
        }
    }


    public void updateStats(View view) {
        updateStats();
    }
}
