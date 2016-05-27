package qcox.tacoma.uw.edu.farmgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import qcox.tacoma.uw.edu.farmgame.data.GameValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValuesDB;
import qcox.tacoma.uw.edu.farmgame.data.SQLiteFarmGame;

/**
 * This class is the major activity in the project.
 * It's the farm filed that player can harvest, and go to shop/silo and high score activity to do other activity.
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class FarmActivity extends AppCompatActivity implements FarmFragment.OnFragmentInteractionListener,
        ItemListFragment.OnListFragmentInteractionListener, AdapterView.OnItemClickListener,
        Communicater{

    int mPos;
    BaseAdapterHelper_farmField mAdapter;
    Bundle myBundle;
    SQLiteFarmGame mySQLite;

    //use to fix double click bug
    int levleUpPosition;
    View levelUpView;
    AdapterView<?> levelUpParent;
    long levelUpId;

//    static int GameValues.getCurrentPlayerValues().getLevel() = 0;
//    static int GameValues.getCurrentPlayerValues().getExp() = 0;
//    static int GameValues.getCurrentPlayerValues().getMoney() = Config.INITIALMONEY;
//    static PlayerValues mPlayerValues;
//    static Map<String, Integer> mInventory ;



    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        myBundle = new Bundle();
//        mInventory = new HashMap<>();
//        GameValues.getCurrentPlayerValues().getLevel() = 0;
//        GameValues.getCurrentPlayerValues().getExp() = 0;
//        GameValues.getCurrentPlayerValues().getMoney() = Config.INITIALMONEY;
        mySQLite = new SQLiteFarmGame(this);
        getLatestPlayerValues();
    }

    //this checks that the latest player values are loaded
    private void getLatestPlayerValues(){
        if (GameValues.mUsername == null) {
            GameValues.mUsername = "guest@guest.com";
        }


        if (GameValues.getCurrentPlayerValues() == null){
            PlayerValues theLocalValues = mySQLite.getLocalPlayerValues();
            //PlayerValues theServerValues = getFromServerDatabase
            if (theLocalValues != null) {
                GameValues.setCurrentPlayerValues(theLocalValues);
            } else {
                GameValues.setCurrentPlayerValues(new PlayerValues(GameValues.mUsername));
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
        return super.onOptionsItemSelected(item);
        //-------------------------------------------------------
    }



    @Override
    public void onStart(){
        super.onStart();
        if (findViewById(R.id.fragment_container)!= null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FarmFragment())
                    .commit();

        }
        PlayerValuesDB theTask = new PlayerValuesDB();
        theTask.GetUserMoney(this);
    }

    /**
     * this is used to starts the high score activity when the highscore button is pressed.
     * @param v the view that called the method (the button)
     */
    public void viewHighScores(View v) {
        Intent intent = new Intent(getApplicationContext(), HighScoreActivity.class);
        startActivity(intent);
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
            PlayerValuesDB theTask = new PlayerValuesDB();
            theTask.UpdateUserMoney(this, GameValues.getCurrentPlayerValues().getMoney());
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
            PlayerValuesDB theTask = new PlayerValuesDB();
            theTask.UpdateUserMoney(this, GameValues.getCurrentPlayerValues().getMoney());
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
        levleUpPosition = position;
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


//
//        if (seed.equals(Config.CORN)){
//            int position = myBundle.getInt("position");
//
//            final Field field = (Field) mAdapter.field_arraylist.get(position);
//            field.imageID = R.drawable.corn_100dp;
//            field.mutureTime = Config.CORNMUTURETIME;
//            field.typeOfCrops = Config.CORN;
//            mAdapter.notifyDataSetChanged();
//            final Handler handler = new Handler();
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    field.mutureTime -= 1000;
//                    mAdapter.notifyDataSetChanged();
//                    if (field.mutureTime > 0){
//                        handler.postDelayed(this, 1000);
//                    }
//                    Log.i("1,mutureTime: "+field.mutureTime, "runnable");
//                }
//            };
//            handler.postDelayed(runnable, 1000);
//            Log.i("2,mutureTime: "+field.mutureTime, "runnable");
//        }
//
//        if (seed.equals(Config.WHEAT)){
//            final int position = myBundle.getInt("position");
//            final Field field = (Field) mAdapter.field_arraylist.get(position);
//            field.imageID = R.drawable.wheat_100dp;
//            field.mutureTime = Config.WHEATMUTURETIME;
//            field.typeOfCrops = Config.WHEAT;
//            mAdapter.notifyDataSetChanged();
//            final Handler handler = new Handler();
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    field.mutureTime -= 1000;
//                    mAdapter.notifyDataSetChanged();
//                    if (field.mutureTime > 0){
//                        handler.postDelayed(this, 1000);
//                    }
//                    Log.i(position + ",muturePlantSeed: "+field.mutureTime, "runnable");
//                }
//            };
//            handler.postDelayed(runnable, 1000);
//
//        }
//        if (seed.equals(Config.STRAWBERRY)){
//            int position = myBundle.getInt("position");
//            final Field field = (Field) mAdapter.field_arraylist.get(position);
//            field.imageID = R.drawable.strawberry_100dp;
//            field.mutureTime = Config.STRAWBERRYMUTURETIME;
//            field.typeOfCrops = Config.STRAWBERRY;
//            mAdapter.notifyDataSetChanged();
//            final Handler handler = new Handler();
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    field.mutureTime -= 1000;
//                    mAdapter.notifyDataSetChanged();
//                    if (field.mutureTime > 0){
//                        handler.postDelayed(this, 1000);
//                    }
//                }
//            };
//            handler.postDelayed(runnable, 1000);
//
//        }
//
//        if (seed.equals(Config.POTATO)){
//            int position = myBundle.getInt("position");
//            final Field field = (Field) mAdapter.field_arraylist.get(position);
//            field.imageID = R.drawable.potato_100dp;
//            field.mutureTime = Config.POTATOMUTURETIME;
//            field.typeOfCrops = Config.POTATO;
//            mAdapter.notifyDataSetChanged();
//            final Handler handler = new Handler();
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    field.mutureTime -= 1000;
//                    mAdapter.notifyDataSetChanged();
//                    if (field.mutureTime > 0){
//                        handler.postDelayed(this, 1000);
//                    }
//                }
//            };
//            handler.postDelayed(runnable, 1000);
//        }
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
//        switch (typeOfCrops){
//            case Config.CORN:{
//                Log.e("2,"+ typeOfCrops+ GameValues.getCurrentPlayerValues().getMoney() +","+ Config.CORNMONEY, "money");
//                GameValues.getCurrentPlayerValues().addExp(Config.CORNEXP);
//                GameValues.getCurrentPlayerValues().setMoney(GameValues.getCurrentPlayerValues().getMoney() + Config.CORNMONEY);
//                Log.e("3,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                return checkLevelUp();
////                Log.i("updateMoney "+typeOfCrops ,"test");
////                break;
//            }
//            case Config.WHEAT:{
//                Log.e("2,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                GameValues.getCurrentPlayerValues().addExp(Config.WHEATEXP);
//                GameValues.getCurrentPlayerValues().getMoney() += Config.WHEATMONEY;
//                Log.e("3,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                return checkLevelUp();
////                Log.i("updateMoney "+typeOfCrops ,"test");
////                break;
//            }
//            case Config.STRAWBERRY:{
//                Log.e("2,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                GameValues.getCurrentPlayerValues().getExp() += Config.STRAWBERRYEXP;
//                GameValues.getCurrentPlayerValues().getMoney() += Config.STRAWBERRYMONEY;
//                Log.e("3,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                return checkLevelUp();
////                Log.i("updateMoney "+typeOfCrops ,"test");
////                break;
//            }
//            case Config.POTATO:{
//                Log.e("2,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                GameValues.getCurrentPlayerValues().getExp() += Config.POTATOEXP;
//                GameValues.getCurrentPlayerValues().getMoney() += Config.POTATOMONEY;
//                Log.e("3,"+ typeOfCrops+GameValues.getCurrentPlayerValues().getMoney()+","+Config.CORNMONEY, "money");
//                return checkLevelUp();
////                Log.i("updateMoney "+typeOfCrops ,"test");
////                break;
//            }
//            default:
//                Log.i("updateMoney default"+typeOfCrops ,"test");
//                return checkLevelUp();
////                break;
//        }
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
        if (GameValues.getCurrentPlayerValues().getExp() >= Config.LEVELUPEXPERIENCEREQUIRED){
            Log.i("level up " + GameValues.getCurrentPlayerValues().getLevel(),"levelup");
            GameValues.getCurrentPlayerValues().addLevel();
            GameValues.getCurrentPlayerValues().addExp(-Config.LEVELUPEXPERIENCEREQUIRED);
//            levelTextView.setText("Lv: "+ GameValues.getCurrentPlayerValues().getLevel());
//            moneyTextView.setText("$: "+ GameValues.getCurrentPlayerValues().getMoney());
//            expTextView.setText("Exp: "+ GameValues.getCurrentPlayerValues().getExp());
            mAdapter = new BaseAdapterHelper_farmField(getApplicationContext(),GameValues.getCurrentPlayerValues().getLevel() * Config.LEVELUPFIELDGAP + Config.INITIALFIELD);

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
                    onItemClick(levelUpParent, levelUpView, levleUpPosition, levelUpId);
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


//            if (seed.equals(Config.CORN)){
//                field.imageID = R.drawable.corn_100dp;
//                field.typeOfCrops = Config.CORN;
//                mAdapter.notifyDataSetChanged();
//                final Handler handler = new Handler();
//                final Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        field.mutureTime -= 1000;
//                        mAdapter.notifyDataSetChanged();
//                        if (field.mutureTime > 0){
//                            handler.postDelayed(this, 1000);
//                        }
//                        Log.i("1,mutureTime: "+field.mutureTime, "runnable");
//                    }
//                };
//                handler.postDelayed(runnable, 1000);
//            }
//
//            if (seed.equals(Config.WHEAT)){
//                field.imageID = R.drawable.wheat_100dp;
//                field.typeOfCrops = Config.WHEAT;
//                mAdapter.notifyDataSetChanged();
//                final Handler handler = new Handler();
//                final Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        field.mutureTime -= 1000;
//                        mAdapter.notifyDataSetChanged();
//                        if (field.mutureTime > 0){
//                            handler.postDelayed(this, 1000);
//                        }
//                    }
//                };
//                handler.postDelayed(runnable, 1000);
//
//            }
//            if (seed.equals(Config.STRAWBERRY)){
//                field.imageID = R.drawable.strawberry_100dp;
//                field.typeOfCrops = Config.STRAWBERRY;
//                mAdapter.notifyDataSetChanged();
//                final Handler handler = new Handler();
//                final Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        field.mutureTime -= 1000;
//                        mAdapter.notifyDataSetChanged();
//                        if (field.mutureTime > 0){
//                            handler.postDelayed(this, 1000);
//                        }
//                    }
//                };
//                handler.postDelayed(runnable, 1000);
//
//            }
//
//            if (seed.equals(Config.POTATO)){
//                field.imageID = R.drawable.potato_100dp;
//                field.typeOfCrops = Config.POTATO;
//                mAdapter.notifyDataSetChanged();
//                final Handler handler = new Handler();
//                final Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        field.mutureTime -= 1000;
//                        mAdapter.notifyDataSetChanged();
//                        if (field.mutureTime > 0){
//                            handler.postDelayed(this, 1000);
//                        }
//                    }
//                };
//                handler.postDelayed(runnable, 1000);
//            }
        }

    }


}
