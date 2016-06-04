package qcox.tacoma.uw.edu.farmgame;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.data.GameValues;
import qcox.tacoma.uw.edu.farmgame.items.PlantItems;

/**
 * This fragment is used to display a list of options when the user wants to plant a seed in the field
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class FieldPlantSeedListDialogFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Communicater mCommunicater;

    private String mParam1;
    private String mParam2;

    private AdapterView<?> parent;
    private View view;
    private int position;
    private long id;


    /**
     * empty constructor
     */
    public FieldPlantSeedListDialogFragment() {
        // Required empty public constructor
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }

    /**
     * create dialog to display a list of seeds to the user
     * @param savedInstanceState
     * @return Dialog
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        List<String> StringList = new ArrayList<>();
//        List<PlantItems> PlantItemsList = GameValues.getPlantItemsList();
//        for (int i = 0; i < PlantItemsList.size(); i++) {
//            StringList.add(PlantItemsList.get(i).name);
//        }
        Iterator iterator = GameValues.getCurrentPlayerValues().getItemMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry map_values = (Map.Entry)iterator.next();
            String name = (String)map_values.getKey();
            int amount = (Integer)map_values.getValue();
            if (amount > 0) {
                StringList.add(name + "(" + amount + ")");
            }
        }
        StringList.add(getString(R.string.close_list));

//        String logcat = "PlantItemsList " + PlantItemsList.size();
//        Log.i(Config.FARM_GAME_LOGCAT, logcat);

        final String[] theCrops = new String[StringList.size()];
        StringList.toArray(theCrops);
        builder.setTitle(R.string.plant_a_seed)
                .setItems(theCrops, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Resources res = getActivity().getResources();
                        //String[] crops = res.getStringArray(R.array.array_crops);

                        //talk to FarmFragment by using FarmActivity as a mCommunicater
                        mCommunicater = (Communicater) getActivity();
                        mCommunicater.plantSeed(theCrops[which]);
                    }
                });

        return builder.create();
    }
}
