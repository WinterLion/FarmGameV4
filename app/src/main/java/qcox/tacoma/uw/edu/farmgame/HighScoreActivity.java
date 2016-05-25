package qcox.tacoma.uw.edu.farmgame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import qcox.tacoma.uw.edu.farmgame.highscore.HighScore;
/**
 * This class is used to display highscore by using listFragment
 * @author james
 * @version 1.0
 * @since 2016-5-4
 */
public class HighScoreActivity extends AppCompatActivity implements HighScoreListFragment.OnListFragmentInteractionListener {

    /**
     * add HighScoreListFragment to HighScoreActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            HighScoreListFragment courseListFragment = new HighScoreListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.highScoreActivity_container, courseListFragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * happened when the one of the listfragment is clicked.
     * add that specific fragment detail to activity.
     * @param item
     */
    @Override
    public void onListFragmentInteraction(HighScore item) {
        HighscoreDetailFragment highscoreDetailFragment = new HighscoreDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(HighscoreDetailFragment.HIGHSCORE_ITEM_SELECTED, item);
        highscoreDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.highScoreActivity_container, highscoreDetailFragment)
                .addToBackStack(null)
                .commit();
    }


}
