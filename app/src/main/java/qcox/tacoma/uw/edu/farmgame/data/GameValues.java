package qcox.tacoma.uw.edu.farmgame.data;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.items.PlantItems;
import qcox.tacoma.uw.edu.farmgame.data.DownloadPlantsTask;

/**
 * Created by Cox Family on 5/24/2016.
 */
public class GameValues {

    public static String mUsername;

    //these are the descriptions of the items
    public static List<PlantItems> mPlantItemsList = new ArrayList<>();

    public static PlayerValues mCurrentPlayerValues;

    public static PlayerValues mServerPlayerValues;

    public static Map<String, Integer> mServerItemMap = new HashMap<>();

    public static PlayerValues getCurrentPlayerValues() {
        return mCurrentPlayerValues;
    }

    public static PlayerValues getServerPlayerValues() {
        return mServerPlayerValues;
    }

    public static void setCurrentPlayerValues(PlayerValues mCurrentPlayerValues) {
        GameValues.mCurrentPlayerValues = mCurrentPlayerValues;
    }

    public static void setServerPlayerValues(PlayerValues mServerPlayerValues) {
        GameValues.mServerPlayerValues = mServerPlayerValues;
    }

    public static PlantItems getPlantItem(int thePos) {
        return mPlantItemsList.get(thePos);
    }

    public static boolean hasPlantItem(String theName) {
        boolean answer = false;

        for (int i = 0; i < mPlantItemsList.size() && !answer; i++) {
           if (theName.equals(mPlantItemsList.get(i).name)){
                answer = true;
            }
        }
        return answer;
    }

    public static PlantItems getPlantItem(String theName) {
        PlantItems answer = null;
        for (int i = 0; i < mPlantItemsList.size() && answer == null; i++) {
            if (theName.equals(mPlantItemsList.get(i).name)){
                answer = mPlantItemsList.get(i);
            }
        }
        return answer;
    }

    public static void setPlantItemsList(List<PlantItems> PlantItemsList) {
        mPlantItemsList = PlantItemsList;
    }


    private static final String PLANTS_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/james.php?cmd=plants";
    public static void checkPlantItemsList(Activity theActivity) {
        if (mPlantItemsList.size() == 0) {
            DownloadPlantsTask task = new DownloadPlantsTask(theActivity);
            task.execute(new String[]{PLANTS_URL});
        }
    }

    public static List<PlantItems> getPlantItemsList() {
        return mPlantItemsList;
    }
}
