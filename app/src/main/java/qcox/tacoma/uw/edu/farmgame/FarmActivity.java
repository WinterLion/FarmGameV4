package qcox.tacoma.uw.edu.farmgame;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.data.GameValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValuesDB;
import qcox.tacoma.uw.edu.farmgame.data.SQLiteFarmGame;
import qcox.tacoma.uw.edu.farmgame.highscore.HighScore;
import qcox.tacoma.uw.edu.farmgame.items.FieldsObject;

/**
 * This class is the major activity in the project.
 * It's the farm filed that player can harvest, and go to shop/silo and high score activity to do other activity.
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class FarmActivity extends AppCompatActivity implements FarmFragment.OnFragmentInteractionListener,
        ItemListFragment.OnListFragmentInteractionListener, AdapterView.OnItemClickListener,
        Communicater, HighScoreListFragment.OnListFragmentInteractionListener{

    private final static String ADD_HIGHSCORE_URL
            = "http://cssgate.insttech.washington.edu/~_450atm17/addHighScore.php?";
    int mPos;
    public static BaseAdapterHelper_farmField mAdapter;
    Bundle myBundle;
    SQLiteFarmGame mySQLite;

    //use to fix double click bug
    int levelUpPosition;
    View levelUpView;
    AdapterView<?> levelUpParent;
    long levelUpId;

//    static int GameValues.getCurrentPlayerValues().getLevel() = 0;
//    static int GameValues.getCurrentPlayerValues().getExp() = 0;
//    static int GameValues.getCurrentPlayerValues().getMoney() = Config.INITIALMONEY;
//    static PlayerValues mPlayerValues;
//    static Map<String, Integer> mInventory ;



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList("field_arraylist", BaseAdapterHelper_farmField.field_arraylist);
        super.onSaveInstanceState(savedInstanceState);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

//        //create empty adapter to
//        mAdapter = new BaseAdapterHelper_farmField();
        if (savedInstanceState == null){
            BaseAdapterHelper_farmField.field_arraylist = new ArrayList<>();
            Log.i("lifecycle onCreate:", "save: null");
        }else{
            BaseAdapterHelper_farmField.field_arraylist = savedInstanceState.getParcelableArrayList("field_arraylist");
            Log.i("lifecycle onCreate:", "save: not null");
        }

        myBundle = new Bundle();
        mySQLite = new SQLiteFarmGame(this);
        getLatestPlayerValues();
        Log.i("GV currentuser: ", GameValues.mUsername);
        Log.i("currentuser: ", GameValues.getCurrentPlayerValues().getUserName());
        if (findViewById(R.id.fragment_container)!= null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FarmFragment())
                    .addToBackStack(null)
                    .commit();

        }

        FieldsObject fieldsObject = mySQLite.loadSQLiteFields(GameValues.getCurrentPlayerValues().getUserName());
        if (fieldsObject != null){
            int size = fieldsObject.getFields().size();
            BaseAdapterHelper_farmField.field_arraylist = new ArrayList<>();
            String typeOfCrops;
            long systemTime;
            int mutureTime = 0;
            int imageID = 0;
            for (int i = 0; i < size; i++){
                typeOfCrops = fieldsObject.getCrop(i);
                systemTime = fieldsObject.getSystemTime(i);
                //crop is not planted yet, empty field
                if (typeOfCrops.equals(Config.FIELD)){
                    systemTime = 0;
                    mutureTime = 0;
                    imageID = R.drawable.field_100dp;
                }
                //crop is planted
                else {
                    //get ImageID
                    try{
                        if (typeOfCrops.equals(Config.CORN)){
                            imageID = R.drawable.corn_100dp;
                            mutureTime = Config.CORNMUTURETIME;
                        }
                        if (typeOfCrops.equals(Config.STRAWBERRY)){
                            imageID = R.drawable.strawberry_100dp;
                            mutureTime = Config.STRAWBERRYMUTURETIME;
                        }
                        if (typeOfCrops.equals(Config.WHEAT)){
                            imageID = R.drawable.wheat_100dp;
                            mutureTime = Config.WHEATMUTURETIME;
                        }
                        if (typeOfCrops.equals(Config.POTATO)){
                            imageID = R.drawable.potato_100dp;
                            mutureTime = Config.POTATOMUTURETIME;
                        }
                    } catch (Exception e){
                        Log.e("lifecycle", "imageID is not initialized");
                    }
//                    if (GameValues.hasPlantItem(typeOfCrops)){
//                        Log.e("works", typeOfCrops);
//                    }

//                    while(GameValues.getPlantItemsList().size() == 0){
//                        try{
//                            wait(20);
//                        } catch (InterruptedException e){
//
//                        }
//                    }
//                    mutureTime = GameValues.getPlantItem(typeOfCrops).growTime;



                    //crop is planted and read to harvest
                    if (System.currentTimeMillis() - systemTime >= mutureTime){
                        systemTime = 0;
                        mutureTime = -1;//muture crop's muture time is less than 0
                    }
                    //crop is planted and not ready to harvest
                    else {
                        mutureTime = (int) (System.currentTimeMillis() - systemTime);
                    }
                }
                Log.i("lifecycle onCreate: ", i + " imageID: "+imageID + "Crops: " + typeOfCrops + "mutureTime: "+ mutureTime + "systemtime: "+ systemTime);
                BaseAdapterHelper_farmField.field_arraylist.add(new Field(imageID, typeOfCrops, mutureTime, systemTime));
            }
        }
    }

    @Override
    public void onStart(){
        Log.i("lifecycle onStart:", "onStart");
        super.onStart();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null){
            BaseAdapterHelper_farmField.field_arraylist = new ArrayList<>();
            Log.i("lifecycle onRestore:", "save: null");
        }else{
            BaseAdapterHelper_farmField.field_arraylist = savedInstanceState.getParcelableArrayList("field_arraylist");
            Log.i("lifecycle onRestore:", "save: not null");
        }
    }


    //this checks that the latest player values are loaded
    private void getLatestPlayerValues(){
        //first make sure we know who the user is
        if (GameValues.mUsername == null) {
            //used for testing
            GameValues.mUsername = "guest@guest.com";
            String text = "Sign In failed using guest account";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        //check the database for PlayerValues
        //getServerDatabasePlayerValues(GameValues.mUsername);

        if (GameValues.getCurrentPlayerValues() == null || (GameValues.getCurrentPlayerValues().getUserName() != GameValues.mUsername)){
            PlayerValues theLocalValues = mySQLite.getLocalPlayerValues();

            if (theLocalValues != null) {
                GameValues.setCurrentPlayerValues(theLocalValues);
            } else {
                if (GameValues.mServerPlayerValues != null){
                    GameValues.setCurrentPlayerValues(GameValues.mServerPlayerValues);
                } else {
                    //both SQLite and mySQL don't have this user so create new one
                    GameValues.setCurrentPlayerValues(new PlayerValues(GameValues.mUsername));
                }

            }
            saveToSQLite();
        }
        GameValues.checkPlantItemsList(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_farmactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //-------------------------------------------------------
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false).commit();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.action_save) {
            saveToServer();
            return true;
        }

        if (id == R.id.action_load) {
            loadFromServer();
            return true;
        }

        if (id == R.id.send_email_menu) {
            //pop a dialog to ask receivers' email
            AlertDialog.Builder alert = new AlertDialog.Builder(FarmActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            alert.setMessage("You can share your game score to your friends")
                    .create();
            final View v = inflater.inflate(R.layout.dialog_sendemail,null);
            alert.setView(v);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditText editText = (EditText) v.findViewById(R.id.send_email);
                    String email = editText.getText().toString();
                    //send email
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
            alert.setNegativeButton("not now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
        //-------------------------------------------------------
    }





    /**
     * this is used to starts the high score activity when the highscore button is pressed.
     * @param v the view that called the method (the button)
     */
    public void viewHighScores(View v) {
        addHighScore(buildHighScoreURL());
        HighScoreListFragment itemFragment = new HighScoreListFragment();
        Bundle args = new Bundle();
        itemFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, itemFragment)
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * this starts the itemFragment which contains a list of items and how much the player has.
     * @param v the view that called the method (the button)
     */
    public void startSiloList(View v) {
        ItemListFragment itemFragment = new ItemListFragment();
        Bundle args = new Bundle();
        itemFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, itemFragment)
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void getServerDatabasePlayerValues(String theUser){
       // PlayerValuesDB theTask = new PlayerValuesDB();
        //theTask.UpdateUserMoney(this, GameValues.getCurrentPlayerValues().getMoney());
    }

    public void saveToServer(){
        PlayerValuesDB theTask = new PlayerValuesDB();
        theTask.UpdatePlayerValuesServer(this, GameValues.getCurrentPlayerValues().getUserName(),
                GameValues.getCurrentPlayerValues().getMoney(), GameValues.getCurrentPlayerValues().getLevel(),
                GameValues.getCurrentPlayerValues().getExp(), GameValues.getCurrentPlayerValues().mScore);
        Iterator iterator = GameValues.getCurrentPlayerValues().getItemMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry map_values = (Map.Entry)iterator.next();
            theTask.UpdateItemServer(this, GameValues.mUsername, (String)map_values.getKey(), (Integer)map_values.getValue());
        }
    }

    public void loadFromServer(){
        PlayerValuesDB theTask = new PlayerValuesDB();
        theTask.GetPlayerValuesServer(this, GameValues.mUsername);
        theTask.GetItemServer(this, GameValues.mUsername, "");
    }

//    public void updateMoneyToServer(){
//        PlayerValuesDB theTask = new PlayerValuesDB();
//        theTask.UpdateUserMoney(this, GameValues.getCurrentPlayerValues().getMoney());
//    }
//
//    public void updateMoneyFromServer(String theUserName){
//        PlayerValuesDB theTask = new PlayerValuesDB();
//        theTask.GetUserMoney(this, theUserName);
//    }

    /**
     * this is when an item has its buy button pressed.
     * @param v the view that called the method (the button)
     */
    public void buyInventoryItems(View v) {
        int cost = GameValues.getPlantItem(mPos).buyCost;
        int numToBuy = 0;
        EditText theNumToBuy = (EditText)findViewById(R.id.num_to_buy);
        if (theNumToBuy != null) {
            String a = theNumToBuy.getText().toString();
            if (!a.isEmpty()) {
                numToBuy = Math.abs(Integer.valueOf(a));
            }
        }
        CharSequence text;
        if (numToBuy < 1) {
            text= "You must pick a valid value for the amount";
        } else if (cost * numToBuy <= GameValues.getCurrentPlayerValues().getMoney()) {
            GameValues.getCurrentPlayerValues().addItemAmount(GameValues.getPlantItem(mPos).name, numToBuy);
            GameValues.getCurrentPlayerValues().setMoney(GameValues.getCurrentPlayerValues().getMoney() - cost * numToBuy);
            text = "You just bought " + numToBuy + " " + GameValues.getPlantItem(mPos).name
                    + " for " + cost * numToBuy + " coins.";
            saveToSQLite();
        } else {
            text = "You don't have enough money!";
        }

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        super.onBackPressed();
    }

    /**
     * this is when an item has its sell button pressed.
     * @param v the view that called the method (the button)
     */
    public void sellInventoryItems(View v) {
        int cost = GameValues.getPlantItem(mPos).sellCost;
        int numToSell = 0;
        EditText theNumToSell = (EditText)findViewById(R.id.num_to_sell);
        if (theNumToSell != null) {
            String a = theNumToSell.getText().toString();
            if (!a.isEmpty()) {
                numToSell = Math.abs(Integer.valueOf(a));
            }
        }
        CharSequence text;
        int currentAmount = GameValues.getCurrentPlayerValues().getItemAmount(GameValues.getPlantItem(mPos).name);
        if (numToSell < 1) {
            text= "You must pick a valid value for the amount";
        } else if (numToSell <= currentAmount) {
            GameValues.getCurrentPlayerValues().setItemAmount(GameValues.getPlantItem(mPos).name, currentAmount - numToSell);
            GameValues.getCurrentPlayerValues().setMoney(GameValues.getCurrentPlayerValues().getMoney() + cost * numToSell);
            text = "You just sold " + numToSell + " " + GameValues.getPlantItem(mPos).name
                    + " for " + cost * numToSell + " coins.";
            saveToSQLite();
        } else {
            text = "You don't have enough of that item!";
        }

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    //this is when an inventory item is selected and the details need to be viewed.
    @Override
    public void onListFragmentInteraction(int position) {
        mPos = position;
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ItemDetailFragment.ARG_POSITION, position);
        itemDetailFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, itemDetailFragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("onItemClick called ","test");
        mAdapter = (BaseAdapterHelper_farmField) parent.getAdapter();
        FieldPlantSeedListDialogFragment fieldPlantSeedListDialogFragment = new FieldPlantSeedListDialogFragment();
        myBundle.putInt("position", position);
        levelUpView = view;
        levelUpParent = parent;
        levelUpPosition = position;
        levelUpId = id;
        if (BaseAdapterHelper_farmField.field_arraylist.get(position).mutureTime < 0){
            Toast.makeText(getApplicationContext(), "You have harvested your crops", Toast.LENGTH_SHORT).show();
            mAdapter.field_arraylist.get(position).imageID = R.drawable.field_100dp;
            mAdapter.notifyDataSetChanged();
            Log.i("onItemClick notify","test");
            boolean levelUp = updateMoneyExpHarvest(mAdapter.field_arraylist.get(position).typeOfCrops);
            Log.i("onItemClick boolean" + levelUp,"test");
            Log.i("onItemClick updateMoney","test");
            //initial the field
            BaseAdapterHelper_farmField.field_arraylist.get(position).mutureTime = 0;
            BaseAdapterHelper_farmField.field_arraylist.get(position).typeOfCrops = Config.FIELD;
            Log.i("onItemClick muture = 0","test");
//            if (levelUp){
//                mAdapter.getView(position,view,parent).performClick();
//                Log.i("onItemClick click","test");
//            }
        }
        else{
            fieldPlantSeedListDialogFragment.show(getSupportFragmentManager(), "what is this parameter?");
        }
    }



    @Override
    public void plantSeed(String seed) {
        Log.i("plantSeed called ","test");
        int checkParen = seed.indexOf("(");
        if (checkParen != -1) {
            seed = seed.substring(0, checkParen);
        }
        if (GameValues.hasPlantItem(seed)) {
            int position = myBundle.getInt("position");
            final Field field = (Field) mAdapter.field_arraylist.get(position);
            int imageResourceIndex = -1;
            String imageName = GameValues.getPlantItem(seed).imageName + "_100dp";
            //resource used: http://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
            try {
                java.lang.reflect.Field idField = R.drawable.class.getDeclaredField(imageName);
                imageResourceIndex = idField.getInt(idField);
                field.imageID = imageResourceIndex;
            } catch (Exception e) {
                //image will be -1 if no image found.
            }

            GameValues.getCurrentPlayerValues().addItemAmount(seed, -1);

            //field.imageID = R.drawable.corn_100dp;
            field.mutureTime = GameValues.getPlantItem(seed).growTime;
            field.systemCurrentTime = System.currentTimeMillis();
            field.typeOfCrops = seed;
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
            Log.i("2,mutureTime: "+field.mutureTime, "runnable");
        } else if (seed == getString(R.string.close_list)) {
            //do nothing
        } else {
            Log.i("bad seed name ", seed);
            String text = "Error: bad seed name " + seed;
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean updateMoneyExpHarvest(String typeOfCrops){
        Log.i("updateMoney called ","test");
        if (GameValues.hasPlantItem(typeOfCrops)){
            GameValues.getCurrentPlayerValues().addMoney(GameValues.getPlantItem(typeOfCrops).sellCost);
            GameValues.getCurrentPlayerValues().addExp(GameValues.getPlantItem(typeOfCrops).exp);
        } else if (typeOfCrops == Config.FIELD) {
            //do nothing
        } else {
            Log.i("bad typeOfCrops ",typeOfCrops);
            String text = "Error: bad typeOfCrops " + typeOfCrops;
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        return checkLevelUp();
    }

    private void updateTop(){
        TextView levelTextView = (TextView) findViewById(R.id.level_textView);
        TextView moneyTextView = (TextView) findViewById(R.id.money_textView);
        TextView expTextView = (TextView) findViewById(R.id.experience_textView);
        levelTextView.setText("Lv: "+ GameValues.getCurrentPlayerValues().getLevel());
        moneyTextView.setText("$: "+ GameValues.getCurrentPlayerValues().getMoney());
        expTextView.setText("Exp: "+ GameValues.getCurrentPlayerValues().getExp());
    }

    public void saveToSQLite() {
        mySQLite.setLocalPlayerValues(GameValues.getCurrentPlayerValues());
    }

    public boolean checkLevelUp(){
        boolean answer = false;
//        TextView levelTextView = (TextView) findViewById(R.id.level_textView);
//        TextView moneyTextView = (TextView) findViewById(R.id.money_textView);
//        TextView expTextView = (TextView) findViewById(R.id.experience_textView);

        Log.i("checkLevelUp called ","test");
        if (GameValues.getCurrentPlayerValues().getExp() >= Config.LEVELUP_EXPERIENCE_REQUIRED){
            Log.i("level up " + GameValues.getCurrentPlayerValues().getLevel(),"levelup");
            GameValues.getCurrentPlayerValues().addLevel();
            GameValues.getCurrentPlayerValues().addExp(-Config.LEVELUP_EXPERIENCE_REQUIRED);
//            levelTextView.setText("Lv: "+ GameValues.getCurrentPlayerValues().getLevel());
//            moneyTextView.setText("$: "+ GameValues.getCurrentPlayerValues().getMoney());
//            expTextView.setText("Exp: "+ GameValues.getCurrentPlayerValues().getExp());
            mAdapter = new BaseAdapterHelper_farmField(getApplicationContext(),GameValues.getCurrentPlayerValues().getLevel() * Config.LEVELUP_FIELD_GAP + Config.INITIAL_FIELD);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Level Up! You earn an extra field! Congrat!")
                    .create();
            alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){

                /**
                 * This method will be invoked when a button in the dialog is clicked.
                 *
                 * @param dialog The dialog that received the click.
                 * @param which  The button that was clicked (e.g.
                 *               {@link DialogInterface#BUTTON1}) or the position
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onItemClick(levelUpParent, levelUpView, levelUpPosition, levelUpId);
                }
            });
            alert.setCancelable(false);
            alert.show();
            Log.i("checkLvUp newAdapter" ,"test");
            levelUpNewAdapterAnimation();
            Log.i("checkLvUp Animation" ,"test");
            answer = true;
        }
//        levelTextView.setText("Lv: "+ GameValues.getCurrentPlayerValues().getLevel());
//        moneyTextView.setText("$: "+ GameValues.getCurrentPlayerValues().getMoney());
//        expTextView.setText("Exp: "+ GameValues.getCurrentPlayerValues().getExp());
        updateTop();
        saveToSQLite();
        Log.i("checkLvUp last" ,"test");
        return answer;
    }

    /**
     * Continue animation of the crops since new adater is created and all previous animation is gone.
     *
     */
    public void levelUpNewAdapterAnimation() {
        Log.i("Animation called ","test");
        for (int i = 0; i < mAdapter.field_arraylist.size(); i++){
            final Field field = mAdapter.field_arraylist.get(i);
            String seed = field.typeOfCrops;
            if (GameValues.hasPlantItem(seed)) {
                String imageName = GameValues.getPlantItem(seed).imageName + "_100dp";
                //resource used: http://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
                try {
                    java.lang.reflect.Field idField = R.drawable.class.getDeclaredField(imageName);
                    field.imageID =  idField.getInt(idField);
                } catch (Exception e) {
                    //image will be -1 if no image found.
                }
                field.typeOfCrops = seed;
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
                .replace(R.id.fragment_container, highscoreDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    public int calculateHighScore(){
        int money = GameValues.getCurrentPlayerValues().getMoney();
        int level = GameValues.getCurrentPlayerValues().getLevel();
        int exp = GameValues.getCurrentPlayerValues().getExp();
        int highScore = level * 1000 + exp + money * 50;
        return highScore;
    }
    private String buildHighScoreURL() {
        StringBuilder sb = new StringBuilder(ADD_HIGHSCORE_URL);
        try {
            String username = GameValues.getCurrentPlayerValues().getUserName();
            sb.append("username=");
            sb.append(URLEncoder.encode(username, "UTF-8"));

            int highscore = calculateHighScore();
            sb.append("&highscore=");
            sb.append(highscore);

            Log.i("addHighScore", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(), "addHighScore wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    private class AddHighScoreTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    Log.i("123 String URL is: ",url);
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                        Log.i("123 String response: ",response);
                    }

                } catch (Exception e) {
                    response = "Unable to add highScore, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addHighScore(String url){
        AddHighScoreTask task = new AddHighScoreTask();
        task.execute(new String[]{url.toString()});
    }

    @Override
    protected void onStop() {
        Log.i("lifecycle onStop:", "onStop");
        super.onStop();
        String username = GameValues.getCurrentPlayerValues().getUserName();
        mySQLite.deleteFieldTable();
        FieldsObject fieldsObject = new FieldsObject(username);
        for (int i = 0; i < BaseAdapterHelper_farmField.field_arraylist.size(); i++){
            String crop = BaseAdapterHelper_farmField.field_arraylist.get(i).typeOfCrops;
            long systemCurrentTime = BaseAdapterHelper_farmField.field_arraylist.get(i).systemCurrentTime;
            fieldsObject.addField(i,crop, systemCurrentTime);
            Log.i("lifecycle onStop:", "onStop: " + i +" "+ BaseAdapterHelper_farmField.field_arraylist.get(i).typeOfCrops + "systemtime: "+ systemCurrentTime);
        }
        mySQLite.saveSQLiteFields(fieldsObject);

    }
    @Override
    protected void onDestroy() {
        Log.i("lifecycle onDestroy:", "onDestroy");
        super.onDestroy();

    }
    @Override
    protected void onPause() {
        Log.i("lifecycle onPause:", "onPause");
        super.onPause();
    }
    @Override
    protected void onResume() {
        Log.i("lifecycle onResume:", "onResume");
        super.onResume();
    }
    @Override
    protected void onRestart() {
        Log.i("lifecycle onRestart:", "onRestart");
        super.onRestart();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.i("lifecycle onAttachFrag:", "onAttachFragment");
        super.onAttachFragment(fragment);
    }
}
