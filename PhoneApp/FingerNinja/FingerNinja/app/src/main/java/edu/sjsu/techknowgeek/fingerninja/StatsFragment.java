package edu.sjsu.techknowgeek.fingerninja;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StatsFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static final String TAB_TITLE = "Statistics";

    private static View rootView;

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
        return rootView;
    }

    public static void updateStats() {
        String[] scores = NetworkManager.getGameStats("userName");
        // Updates Android UI with scores from hashmap
        TextView avgOS = (TextView) rootView.findViewById(R.id.hs_avg_overall_score);
        TextView uLastHrS = (TextView) rootView.findViewById(R.id.hs_avg_ulasthr_score);
        TextView uLastWkS = (TextView) rootView.findViewById(R.id.hs_avg_ulastwk_score);
        TextView uLastMS = (TextView) rootView.findViewById(R.id.hs_avg_ulastm_score);
        TextView overallS = (TextView) rootView.findViewById(R.id.hs_overall_score);
        TextView userS = (TextView) rootView.findViewById(R.id.hs_user_score);

        userS.setText(scores[1]);
        overallS.setText(scores[2]);
        avgOS.setText(scores[3]);
        uLastHrS.setText(scores[4]);
        uLastWkS.setText(scores[5]);
        uLastMS.setText(scores[6]);
    }
}
