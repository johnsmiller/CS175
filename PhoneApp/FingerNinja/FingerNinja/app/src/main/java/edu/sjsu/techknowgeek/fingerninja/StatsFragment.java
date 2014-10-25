package edu.sjsu.techknowgeek.fingerninja;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class StatsFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static final String TAB_TITLE = "Statistics";

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
        View rootView = inflater.inflate(R.layout.activity_stats, container, false);
        return rootView;
    }

    public void updateStats() {

    }

}
