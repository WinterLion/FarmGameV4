package qcox.tacoma.uw.edu.farmgame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import qcox.tacoma.uw.edu.farmgame.highscore.HighScore;


/**
 * This class is used to display the fragment detail in the listfragment
 * @author james
 * @version 1.0
 * @since 2016-5-4
 */
public class HighscoreDetailFragment extends Fragment {


    private TextView mUsernameTextView;
    private TextView mHighScoreTextView;
    public static String HIGHSCORE_ITEM_SELECTED = "highScoreItemSelected";

    /**
     * constructor
     */
    public HighscoreDetailFragment() {
        // Required empty public constructor
    }

    /**
     * create fragment view, and get username TextView and highscore TextView
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_highscore_detail, container, false);
        mUsernameTextView = (TextView) view.findViewById(R.id.highScoreFragment_username);
        mHighScoreTextView = (TextView) view.findViewById(R.id.highScoreFragment_highscore);

        return view;
    }

    /**
     * update highscore
     * @param highScore
     */
    public void updateView(HighScore highScore) {
        if (highScore != null) {
            mUsernameTextView.setText(highScore.getUsername());
            mHighScoreTextView.setText(highScore.getHighscore());
        }
    }

    /**
     * During startup, check if there are arguments passed to the fragment.
     * onStart is a good place to do this because the layout has already been
     * applied to the fragment at this point so we can safely call the method
     * below that sets the article text.
     */
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((HighScore) args.getSerializable(HIGHSCORE_ITEM_SELECTED));
        }
    }


}
