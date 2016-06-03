package qcox.tacoma.uw.edu.farmgame;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import qcox.tacoma.uw.edu.farmgame.data.GameValues;


/**
 * The main fragment to perform farming action by using customized base adapter.
 * The BaseApater and Field classes exists in the same java file
 * because the three class are connected and only can be used as a whole.
 * FarmFragment contains the farm fields, player's states(level, money, experience), and connection to highscore and silo/shop
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class FarmFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;
    BaseAdapterHelper_farmField mAdapter;

    /**
     * empty constructor
     */
    public FarmFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     * creates the view, load game states to the adapter
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null){
            Log.i("lifecycle onCreateView:", "save: null fragment");
        } else {
            Log.i("lifecycle onCreateView:", "save: not null fragment");
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_farm, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridView);
//        if (mAdapter == null){
//            Log.i("adapter:", "Adpter == null");
//        }
//        if (FarmActivity.mAdapter == null){
//            Log.i("adapter", "Farm.Adpter == null");
//        }
        if (FarmActivity.mAdapter == null || LoginActivity.logInCount >1){
//            Log.i("lifecycle onCreateView:", "Adpter == null");
            mAdapter = new BaseAdapterHelper_farmField(getContext(), GameValues.getCurrentPlayerValues().getLevel() * Config.LEVELUP_FIELD_GAP + Config.INITIAL_FIELD);
            if (LoginActivity.logInCount > 1){
                LoginActivity.logInCount = 1;//use to fix rotate losing game states bug
            }
            for (int i = 0; i < mAdapter.field_arraylist.size(); i++){
                final Field field = (Field) mAdapter.field_arraylist.get(i);
                if (!field.typeOfCrops.equals(Config.FIELD)){
                    mAdapter.notifyDataSetChanged();
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            field.mutureTime -= 1000;
                            mAdapter.notifyDataSetChanged();
                            if (field.mutureTime > 0){
                                handler.postDelayed(this, 1000);
                            }
                            Log.i("1,mutureTime: "+field.mutureTime, "runnable");
                        }
                    };
                    handler.postDelayed(runnable, 1000);
                }
            }

        }
        else{
            Log.i("adapter", "FarmActivity.mAdapter != null");
            mAdapter = FarmActivity.mAdapter;
            mAdapter.notifyDataSetChanged();
        }
        TextView moneyTextView = (TextView) v.findViewById(R.id.money_textView);
        moneyTextView.setText("$: "+ GameValues.getCurrentPlayerValues().getMoney());
        TextView levelTextView = (TextView) v.findViewById(R.id.level_textView);
        levelTextView.setText("Lv: "+ GameValues.getCurrentPlayerValues().getLevel());
        TextView expTextView = (TextView) v.findViewById(R.id.experience_textView);
        expTextView.setText("Exp: "+ GameValues.getCurrentPlayerValues().getExp());
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());
        return v;
    }


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onAttach(Context context) {
        Log.i("lifecycle onAttach:", "onAttach fragment");
        super.onAttach(context);
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("lifecycle onCreate:", "onCreate fragment");
        super.onCreate(savedInstanceState);
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("lifecycle onActivityCre", "onActivityCreated fragment");
        super.onActivityCreated(savedInstanceState);
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onStart() {
        Log.i("lifecycle onStart", "onStart fragment");
        super.onStart();
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onResume() {
        Log.i("lifecycle onResume", "onResume fragment");
        super.onResume();
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onPause() {
        Log.i("lifecycle onPause", "onPause fragment");
        super.onPause();
    }
    /**
     * {@inheritDoc}
     * save game states
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("field_arraylist",BaseAdapterHelper_farmField.field_arraylist);
        if (outState == null){
            Log.i("lifecycle onSaveInstan", "onSaveInstanceState bundle null fragment");
        }else{
            Log.i("lifecycle onSaveInstan", "onSaveInstanceState bundle not null fragment");
        }
        super.onSaveInstanceState(outState);
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onStop() {
        Log.i("lifecycle onStop", "onStop fragment");
        super.onStop();
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onDestroyView() {
        Log.i("lifecycle onDestroyView", "onDestroyView fragment");
        super.onDestroyView();
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onDestroy() {
        Log.i("lifecycle onDestroy", "onDestroy fragment");
        super.onDestroy();
    }
    /**
     * {@inheritDoc}
     * also for learning the life cycle
     */
    @Override
    public void onDetach() {
        Log.i("lifecycle onDetach", "onDetach fragment");
        super.onDetach();
    }
    /**
     * {@inheritDoc}
     * reload game states
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null){
            Log.i("lifecycle onViewStateRe", "onViewStateRestored bundle null fragment");
        }else{
            Log.i("lifecycle onViewStateRe", "onViewStateRestored bundle not null fragment");
            BaseAdapterHelper_farmField.field_arraylist = savedInstanceState.getParcelableArrayList("field_arraylist");
        }
        super.onViewStateRestored(savedInstanceState);

    }
}

/**
 * Field object contains the information of the single field
 * It implements Parcelable so that can be saved within Bundle for keeping user's game states when quit the game and come back.
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
class Field implements Parcelable {
    int imageID;
    String typeOfCrops;
    int mutureTime;
    long systemCurrentTime;
    Field(int imageID, String typeOfCrops, int mutureTime, long systemCurrentTime){
        this.imageID = imageID;
        this.typeOfCrops = typeOfCrops;
        this.mutureTime = mutureTime;
        this.systemCurrentTime = systemCurrentTime;
    }

    /**
     * constructor
     * @param in
     */
    private Field(Parcel in) {
        typeOfCrops = in.readString();
        imageID = in.readInt();
        mutureTime = in.readInt();
        systemCurrentTime = in.readLong();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeOfCrops);
        dest.writeInt(imageID);
        dest.writeInt(mutureTime);
        dest.writeLong(systemCurrentTime);
    }

    /**
     * create Parcelable object
     */
    public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        public Field[] newArray(int size) {
            return new Field[size];
        }
    };
}

/**
 * Customize BaseApater to contain timer and picture
 * BaseApater is userd to build farm fields
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
class BaseAdapterHelper_farmField extends BaseAdapter{

    public static ArrayList<Field> field_arraylist;
    Context context;

    BaseAdapterHelper_farmField(){

    }

    /**
     * constructor
     * @param context
     * @param numOfField
     */
    BaseAdapterHelper_farmField(Context context, int numOfField){
        this.context = context;
        if (field_arraylist == null){
            for (int i = 0; i < numOfField; i++){
                field_arraylist.add(new Field(R.drawable.field_100dp, Config.FIELD, 0, 0));
                Log.i("lifecycle BaseApater: ", "field_arraylist == null, add new field: "+ i);
            }
        }
        else {
            for (int i = 0; i < numOfField; i++){
                if (field_arraylist.size() > i){
                    field_arraylist.set(i, new Field(field_arraylist.get(i).imageID,
                            field_arraylist.get(i).typeOfCrops, field_arraylist.get(i).mutureTime,
                            field_arraylist.get(i).systemCurrentTime));
//                    Log.i("lifecycle BaseApater: ", "field_arraylist != null, update new field: "+ i
//                            + " Crops: "+ field_arraylist.get(i).typeOfCrops
//                            + " mutureTime: "+ field_arraylist.get(i).mutureTime
//                            + " systemTime: " + field_arraylist.get(i).systemCurrentTime);
                }
                else {
                    field_arraylist.add(new Field(R.drawable.field_100dp, Config.FIELD, 0, 0));
//                    Log.i("lifecycle BaseApater: ", "i = "+i+","+field_arraylist.size()+"levelup");
                }

            }
        }

    }



    /**
     * Get a View that displays the field at the specified position in the farm fields.
     * {@inheritDoc}
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fieldView = convertView;
        ViewHolder viewHolder = null;
        if (fieldView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            fieldView = layoutInflater.inflate(R.layout.single_field, parent, false);
            viewHolder = new ViewHolder(fieldView);
            fieldView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) fieldView.getTag();
        }
        Field tempField = field_arraylist.get(position);
        viewHolder.myField_ImageView.setImageResource(tempField.imageID);
        if (tempField.mutureTime != 0){
            viewHolder.myTimer_TextView.setText(tempField.mutureTime/1000+"s");
        }
        if (tempField.mutureTime< 0){
            viewHolder.myTimer_TextView.setText("Ready!");
        }
        if (tempField.mutureTime == 0){
            viewHolder.myTimer_TextView.setText("");
        }

        return fieldView;
    }

    /**
     * change the default value to false to disable the field if the crops is not ready to harvest.
     * @return false
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     * if the crop is ready to harvest, the user can click the field. Otherwise, the field is disable.
     */
    @Override
    public boolean isEnabled(int position) {
        // Return true for clickable, false for not
        if (BaseAdapterHelper_farmField.field_arraylist.get(position).mutureTime < 0){
            return true;
        }else if (BaseAdapterHelper_farmField.field_arraylist.get(position).mutureTime == 0){
            return true;
        }else{
            return false;
        }
    }


    /**
     * used to recycle View objects to speed up when a lot of Views exist in the farm
     */
    class ViewHolder{
        ImageView myField_ImageView;
        TextView myTimer_TextView;
        ViewHolder(View v){
            myField_ImageView = (ImageView) v.findViewById(R.id.imageView_field);
            myTimer_TextView = (TextView) v.findViewById(R.id.timer_textView);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return field_arraylist.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position) {
        return field_arraylist.get(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}