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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //this is the URL to get the PVs the player currently has.
    private static final String GET_PLAYER_VALUE_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/playervalues.php?cmd=getplayervalues";
    //this is the URL to set the PVs the player has onto the server database.
    private static final String SET_PLAYER_VALUE_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/playervalues.php?cmd=setplayervalues";
    //this is the URL to get the PVs the player currently has.
    private static final String GET_ITEM_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/playervalues.php?cmd=getitemamounts";
    //this is the URL to set the PVs the player has onto the server database.
    private static final String SET_ITEM_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/playervalues.php?cmd=setitemamount";

    private Activity mActivity;

    /**
     * this method gets the amount of money the user has from the server database
     * @param theActivity this is used for publishing toasts
     */
    public void GetPlayerValuesServer(Activity theActivity, String username) {
        mActivity = theActivity;
        DownloadPlayerValuesTask task = new DownloadPlayerValuesTask();
        task.execute(GET_PLAYER_VALUE_URL + "&user=" + username);
    }

    /**
     * This method will start an async task that updates the server database with the new player values.
     *
     * @param theActivity this is needed to allow toasts
     * @param username this is the amount that will be put on the server database.
     */
    public void UpdatePlayerValuesServer(Activity theActivity, String username, int money,
                                int level, int exp, int score) {
        mActivity = theActivity;
        UploadPlayerValuesTask task = new UploadPlayerValuesTask();
        StringBuilder theFullURL = new StringBuilder();
        theFullURL.append(SET_PLAYER_VALUE_URL);
        theFullURL.append("&user=");
        theFullURL.append(username);
        theFullURL.append("&money=");
        theFullURL.append(money);
        theFullURL.append("&level=");
        theFullURL.append(level);
        theFullURL.append("&exp=");
        theFullURL.append(exp);
        theFullURL.append("&score=");
        theFullURL.append(score);
        task.execute(new String[]{theFullURL.toString()});
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns amount of item if success.
     * @param amountJSON this is the string returned from the php file.
     * @return reason or null if successful.
     */
    public static String parseGetPVJSONJSON(String amountJSON) {
        String reason = null;
        if (amountJSON != null) {
            try {
                JSONArray arr = new JSONArray(amountJSON);
                JSONObject obj = arr.getJSONObject(0);
                //username, money, level, experience, score
                String username = (obj.getString("username"));
                int money = Integer.parseInt(obj.getString("money"));
                int level = Integer.parseInt(obj.getString("level"));
                int experience = Integer.parseInt(obj.getString("experience"));
                int score = Integer.parseInt(obj.getString("score"));

                Map<String, Integer> ItemMap = new HashMap<>();
                GameValues.setServerPlayerValues(new PlayerValues(username, money, level,
                        experience, score, ItemMap));

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
    public static String parseSetPVJSONJSON(String amountJSON) {
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
    private class DownloadPlayerValuesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            result = PlayerValuesDB.parseGetPVJSONJSON(result);
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
    private class UploadPlayerValuesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            result = PlayerValuesDB.parseSetPVJSONJSON(result);
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

    /**
     * this method gets the amount of money the user has from the server database
     * @param theActivity this is used for publishing toasts
     */
    public void GetItemServer(Activity theActivity, String username, String itemname) {
        mActivity = theActivity;
        DownloadItemAmountTask task = new DownloadItemAmountTask();
        task.execute(GET_ITEM_URL + "&user=" + username + "&itemname=" + itemname);
    }

    /**
     * This method will start an async task that updates the server database with the new player values.
     *
     * @param theActivity this is needed to allow toasts
     * @param username this is the amount that will be put on the server database.
     */
    public void UpdateItemServer(Activity theActivity, String username, String itemname,
                                         int amount) {
        mActivity = theActivity;
        UploadItemAmountTask task = new UploadItemAmountTask();
        StringBuilder theFullURL = new StringBuilder();
        theFullURL.append(SET_ITEM_URL);
        theFullURL.append("&user=");
        theFullURL.append(username);
        theFullURL.append("&itemname=");
        theFullURL.append(itemname);
        theFullURL.append("&amount=");
        theFullURL.append(amount);
        task.execute(new String[]{theFullURL.toString()});
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns amount of item if success.
     * @param amountJSON this is the string returned from the php file.
     * @return reason or null if successful.
     */
    public static String parseGetItemJSONJSON(String amountJSON) {
        String reason = null;
        if (amountJSON != null) {
            try {
                GameValues.mServerItemMap = new HashMap<>();
                JSONArray arr = new JSONArray(amountJSON);
                for (int i = 0; i < arr.length() - 1; i++){
                JSONObject obj = arr.getJSONObject(i);
                //username, money, level, experience, score
                String itemname = (obj.getString("itemname"));
                int amount = Integer.parseInt(obj.getString("amount"));
                GameValues.mServerItemMap.put(itemname, amount);
                }

//                JSONObject obj = arr.getJSONObject(0);
//                //username, money, level, experience, score
//                String itemname = (obj.getString("itemname"));
//                int amount = Integer.parseInt(obj.getString("amount"));
//                GameValues.mServerItemMap.put(itemname, amount);
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
    public static String parseSetItemJSONJSON(String amountJSON) {
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
    private class DownloadItemAmountTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            result = PlayerValuesDB.parseGetItemJSONJSON(result);
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
    private class UploadItemAmountTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            result = PlayerValuesDB.parseSetItemJSONJSON(result);
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
