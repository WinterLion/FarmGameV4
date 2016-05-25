package qcox.tacoma.uw.edu.farmgame.data;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import qcox.tacoma.uw.edu.farmgame.items.PlantItems;

/**
 * This task downloads the list of Plants from the database and stores them in a local class and
 * displays them in the recycle viewer.
 */
public class DownloadPlantsTask extends AsyncTask<String, Void, String> {

    private Activity myActivity;

    DownloadPlantsTask(Activity theActivity){
        myActivity = theActivity;
    }

    @Override
    protected void onPostExecute(String result) {
        // Something wrong with the network or the URL.
        if (result.startsWith("Unable to")) {
            Toast.makeText(myActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        List<PlantItems> plantItemsList = new ArrayList<>();
        result = PlantItems.parsePlantsJSONJSON(result, plantItemsList);
        // Something wrong with the JSON returned.
        if (result != null) {
            Toast.makeText(myActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Everything is good, show the list of plants.
        if (!plantItemsList.isEmpty()) {
            GameValues.setPlantItemsList(plantItemsList);
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
                response = "Unable to download the list of plants, Reason: "
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
