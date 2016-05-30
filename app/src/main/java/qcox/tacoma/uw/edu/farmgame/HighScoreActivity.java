package qcox.tacoma.uw.edu.farmgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_highscore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //-------------------------------------------------------
        int id = item.getItemId();

        if (id == R.id.send_email_menu) {
            //pop a dialog to ask receivers' email
            AlertDialog.Builder alert = new AlertDialog.Builder(HighScoreActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            alert.setMessage(R.string.pleaseEnterAnEmail)
                    .create();
            final View v = inflater.inflate(R.layout.dialog_sendemail,null);
            alert.setView(v);
            alert.setNegativeButton("not now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditText editText = (EditText) v.findViewById(R.id.send_email);
                    String email = editText.getText().toString();
                    Log.i("email is :  "+email, "email");
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    String[] to = {email};
                    intent.putExtra(Intent.EXTRA_EMAIL, to);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Check Out This Farm Game");
                    intent.putExtra(Intent.EXTRA_TEXT, "How are you my friend? Look how awesome I did in the farm game! I have earned xxx points!");
                    intent.setType("message/rfc822");
                    Intent chooser = Intent.createChooser(intent, "Send Email");
                    startActivity(chooser);

                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
        //-------------------------------------------------------
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
