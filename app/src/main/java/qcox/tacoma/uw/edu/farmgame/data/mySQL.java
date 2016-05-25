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

import qcox.tacoma.uw.edu.farmgame.MyItemRecyclerViewAdapter;
import qcox.tacoma.uw.edu.farmgame.items.PlantItems;

/**
 * Created by Cox Family on 5/24/2016.
 */
public class mySQL {

    private static final String PLANTS_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/james.php?cmd=plants";



    public static void checkPlantItemsList(Activity theActivity) {

//        DownloadPlantsTask task = new DownloadPlantsTask();
//        task.execute(new String[]{PLANTS_URL});
    }



}

