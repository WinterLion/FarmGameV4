package qcox.tacoma.uw.edu.farmgame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Display a simple tutorial to help user play the game
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class HelpFragment extends Fragment {


    /**
     * empty constructor
     */
    public HelpFragment() {
        super();
        this.setRetainInstance(true);
        // Required empty public constructor
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

}
