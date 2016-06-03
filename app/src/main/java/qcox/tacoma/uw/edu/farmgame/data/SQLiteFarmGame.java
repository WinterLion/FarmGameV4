package qcox.tacoma.uw.edu.farmgame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.items.FieldsObject;

/**
 * Created by Cox Family on 5/24/2016.
 */
public class SQLiteFarmGame {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "GameStateDB.db";

    private static final String GAME_STATE_TABLE = "GameState";
    private static final String INVENTORY_TABLE = "PlayerInventory";
    private static final String FIELD_TABLE = "FieldStateTable";

    private GameStateDBHelper mGameStateDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public SQLiteFarmGame(Context context) {
        mGameStateDBHelper = new GameStateDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mGameStateDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param thePlayerValues
     * @return true or false
     */
    public boolean setLocalPlayerValues(PlayerValues thePlayerValues) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", thePlayerValues.getUserName());
        contentValues.put("money", thePlayerValues.getMoney());
        contentValues.put("level", thePlayerValues.getLevel());
        contentValues.put("exp", thePlayerValues.getExp());
        //long rowId = mSQLiteDatabase.insert(GAME_STATE_TABLE, null, contentValues);
        long rowId = mSQLiteDatabase.replace(GAME_STATE_TABLE, null, contentValues);
        boolean answer = rowId != -1;

        Iterator iterator = thePlayerValues.getItemMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry map_values = (Map.Entry)iterator.next();
            contentValues = new ContentValues();
            contentValues.put("username", GameValues.mUsername);
            contentValues.put("item_name", (String)map_values.getKey());
            contentValues.put("item_amount", (Integer)map_values.getValue());
            //rowId = mSQLiteDatabase.insert(INVENTORY_TABLE, null, contentValues);
            rowId = mSQLiteDatabase.replace(INVENTORY_TABLE, null, contentValues);
            answer = answer && rowId != -1;
        }
        return answer;
    }


    /**
     * Returns the list of courses from the local Course table.
     * @return list
     */
    public PlayerValues getLocalPlayerValues() {
        PlayerValues answer = null;
        String[] columns = {
                "username", "money", "level", "exp"
        };

        String userName;
        int money;
        int level;
        int exp;
        Map<String, Integer> ItemMap = new HashMap<>();
        String WhereColumns = "username=?";
        String[] WhereValues = {GameValues.mUsername};
        Cursor cursor = mSQLiteDatabase.query(
                GAME_STATE_TABLE,  // The table to query
                columns,                               // The columns to return
                WhereColumns,                                // The columns for the WHERE clause
                WhereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.moveToFirst()) {
            userName = cursor.getString(0);
            money = cursor.getInt(1);
            level = cursor.getInt(2);
            exp = cursor.getInt(3);

            String[] columns2 = {
                    "id, username, item_name", "item_amount "
            };

            cursor = mSQLiteDatabase.query(
                    INVENTORY_TABLE,  // The table to query
                    columns2,                               // The columns to return
                    WhereColumns,                                // The columns for the WHERE clause
                    WhereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
            if(cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String itemName = cursor.getString(2);
                    int itemAmount = cursor.getInt(3);
                    ItemMap.put(itemName, itemAmount);
                    cursor.moveToNext();
                }
            }
            answer = new PlayerValues(userName, money, level, exp, ItemMap);
        }
        return answer;
    }


    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param theFieldsObject
     * @return true or false
     */
    public int saveSQLiteFields(FieldsObject theFieldsObject) {
        int rows = 0;
        for (int i = 0; i < theFieldsObject.getFields().size(); i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", theFieldsObject.getUsername());
            contentValues.put("position", theFieldsObject.getPosition(i));
            contentValues.put("typeOfCrop", theFieldsObject.getCrop(i));
            contentValues.put("datetime", theFieldsObject.getSystemTime(i));
            long rowId = mSQLiteDatabase.replace(FIELD_TABLE, null, contentValues);
            if (rowId != -1){
                rows++;
            }
        }
        return rows;
    }


    /**
     * Returns the list of courses from the local Fields table.
     * @return list
     */
    public FieldsObject loadSQLiteFields(String theUser) {
        String[] columns = {
                "username", "position", "typeOfCrop", "datetime"
        };
        String userName;
        int onePosition;
        String oneCrop;
        long systemtime;

        String WhereColumns = "username=?";
        String[] WhereValues = {theUser};
        Cursor cursor = mSQLiteDatabase.query(
                FIELD_TABLE,  // The table to query
                columns,                               // The columns to return
                WhereColumns,                                // The columns for the WHERE clause
                WhereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        boolean first = true;
        FieldsObject answer = null;
        if(cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                userName = cursor.getString(0);
                onePosition = cursor.getInt(1);
                oneCrop = cursor.getString(2);
                systemtime = cursor.getLong(3);
                if (first){
                    answer = new FieldsObject(userName);
                    first = false;
                }
                answer.addField(onePosition, oneCrop, systemtime);
                cursor.moveToNext();
            }
        }
        return answer;
    }

    /**
     * Delete all the data from the TABLES
     */
    public void deleteGameState() {
        String WhereColumns = "username=?";
        String[] WhereValues = {GameValues.mUsername};
        mSQLiteDatabase.delete(GAME_STATE_TABLE, WhereColumns, WhereValues);
        mSQLiteDatabase.delete(INVENTORY_TABLE, WhereColumns, WhereValues);
        mSQLiteDatabase.delete(FIELD_TABLE, WhereColumns, WhereValues);
    }

    public void deleteFieldTable() {
        String WhereColumns = "username=?";
        String[] WhereValues = {GameValues.mUsername};
        mSQLiteDatabase.delete(FIELD_TABLE, WhereColumns, WhereValues);
    }


    public void closeDB() {
        mSQLiteDatabase.close();
    }

    class GameStateDBHelper extends SQLiteOpenHelper {

        private static final String CREATE_GAME_STATE_TABLE_SQL =
                "CREATE TABLE IF NOT EXISTS " + GAME_STATE_TABLE
                        + " (username TEXT PRIMARY KEY, money INT, level INT, exp INT)";

        private static final String DROP_GAME_STATE_TABLE_SQL =
                "DROP TABLE IF EXISTS " + GAME_STATE_TABLE;

        private static final String CREATE_INVENTORY_TABLE_SQL =
                "CREATE TABLE IF NOT EXISTS " + INVENTORY_TABLE
                        + " (id INTEGER PRIMARY KEY, username TEXT NOT NULL, item_name TEXT NOT NULL, item_amount INT)";

        private static final String DROP_INVENTORY_TABLE_SQL =
                "DROP TABLE IF EXISTS " + INVENTORY_TABLE;

        private static final String CREATE_FIELD_TABLE_SQL =
                "CREATE TABLE IF NOT EXISTS " + FIELD_TABLE
                        + " (id INTEGER PRIMARY KEY, username TEXT NOT NULL, position INTEGER  NOT NULL, typeOfCrop TEXT, datetime INTEGER)";

        private static final String DROP_FIELD_TABLE_SQL =
                "DROP TABLE IF EXISTS " + FIELD_TABLE;

        public GameStateDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_GAME_STATE_TABLE_SQL);
            sqLiteDatabase.execSQL(CREATE_INVENTORY_TABLE_SQL);
            sqLiteDatabase.execSQL(CREATE_FIELD_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_GAME_STATE_TABLE_SQL);
            sqLiteDatabase.execSQL(DROP_INVENTORY_TABLE_SQL);
            sqLiteDatabase.execSQL(DROP_FIELD_TABLE_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
