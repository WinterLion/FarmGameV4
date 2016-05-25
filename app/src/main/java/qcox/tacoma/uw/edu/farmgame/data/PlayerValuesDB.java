package qcox.tacoma.uw.edu.farmgame.data;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import qcox.tacoma.uw.edu.farmgame.MyscoreRecyclerViewAdapter;
import qcox.tacoma.uw.edu.farmgame.highscore.HighScore;

/**
 * This class was meant to be used for communicating with the server for various things.  Right now
 * all we have is the communications for the money.
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class PlayerValuesDB {

    //this is the URL to get the money the player currently has.
    private static final String GET_PLAYER_MONEY_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/playervalues.php?cmd=getmoney&user=";
    //this is the URL to set the money the player has onto the server database.
    private static final String SET_PLAYER_MONEY_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/playervalues.php?cmd=setmoney&user=";
    private Activity mActivity;

    /**
     * this method gets the amount of money the user has from the server database
     * @param theActivity this is used for publishing toasts
     */
    public void GetUserMoney(Activity theActivity) {
        mActivity = theActivity;
        DownloadUserMoneyTask task = new DownloadUserMoneyTask();
        task.execute(GET_PLAYER_MONEY_URL + GameValues.getCurrentPlayerValues().getUserName());
    }

    /**
     * This method will start an async task that updates the server database with the new money amount.
     *
     * @param theActivity this is needed to allow toasts
     * @param theNewAmount this is the amount that will be put on the server database.
     */
    public void UpdateUserMoney(Activity theActivity, int theNewAmount) {
        mActivity = theActivity;
        //decided to turn off this feature for now until we figure out the full storage scheme.
        //UploadUserMoneyTask task = new UploadUserMoneyTask();
        //task.execute(new String[]{SET_PLAYER_MONEY_URL + PlayerValues.getUserName() + "&money=" + theNewAmount});

    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns amount of item if success.
     * @param amountJSON this is the string returned from the php file.
     * @return reason or null if successful.
     */
    public static String parseGetAmountJSONJSON(String amountJSON) {
        String reason = null;
        if (amountJSON != null) {
            try {
                JSONArray arr = new JSONArray(amountJSON);
                JSONObject obj = arr.getJSONObject(0);
                int amount = Integer.parseInt(obj.getString("money"));
                GameValues.getCurrentPlayerValues().setMoney(amount);

            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        System.out.print(reason);
        return reason;
    }

    /**
     * this task looks at the return of setting the amount to see if it was a success.
     * @param amountJSON this is the JSON returned from the php.
     * @return the string showing success or failure.
     */
    public static String parseSetAmountJSONJSON(String amountJSON) {
        String reason = null;
        if (amountJSON != null) {
            try {

                JSONObject obj = new JSONObject(amountJSON);
                reason = (obj.getString("result"));
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    /**
     * this task downloads the money the user has.
     */
    private class DownloadUserMoneyTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            result = PlayerValuesDB.parseGetAmountJSONJSON(result);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


    }

    /**
     * this task updates the database with how much money the user has.
     */
    private class UploadUserMoneyTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            result = PlayerValuesDB.parseSetAmountJSONJSON(result);
            // Something wrong with the JSON returned.
            if (result != null && result.equals("success")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }
    }
}
