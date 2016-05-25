package qcox.tacoma.uw.edu.farmgame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class will eventually store the users inventory on their local device.
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class SiloDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "farminggame.db";

    private SiloDBHelper mSiloDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public SiloDB(Context context) {
        mSiloDBHelper = new SiloDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mSiloDBHelper.getWritableDatabase();
    }



    /**
     * Inserts the inventory into the local sqlite table. Returns true if successful, false otherwise.
     * @param id  this is the id of the item
     * @param name this is the name of the item
     * @param itemType this is the type of the item
     * @param itemTypeDetail this is for if there is more needed info about the item
     * @param quantity this is how much of the item the user has
     * @param buyCost this is the buy cost
     * @param sellCost this is the sell cost
     * @param growTime this is how long it takes to grow
     * @param description this is a longer description of the item
     * @param imageName this is the name of the item in the file directory so it can be viewed.
     * @return true or false
     */
    public boolean insertInventory(String id, String name, String itemType, String itemTypeDetail
                                 ,Integer quantity, Integer buyCost, Integer sellCost, Integer growTime
                                 ,Integer exp, String description, String imageName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("itemType", itemType);
        contentValues.put("itemTypeDetail", itemTypeDetail);
        contentValues.put("quantity", quantity);
        contentValues.put("buyCost", buyCost);
        contentValues.put("sellCost", sellCost);
        contentValues.put("growTime", growTime);
        contentValues.put("exp", exp);
        contentValues.put("description", description);
        contentValues.put("imageName", imageName);

        long rowId = mSQLiteDatabase.insert("Inventory", null, contentValues);
        return rowId != -1;
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }


    class SiloDBHelper extends SQLiteOpenHelper {

        private static final String CREATE_INVENTORY_SQL =
                "CREATE TABLE IF NOT EXISTS Inventory "
                        + "(id TEXT PRIMARY KEY, name TEXT, itemType TEXT, itemTypeDetail TEXT, " +
                        "quantity INTEGER, buyCost INTEGER, sellCost INTEGER, growTime INTEGER, exp INTEGER, " +
                        "description TEXT, imageName TEXT)";

        private static final String DROP_INVENTORY_SQL =
                "DROP TABLE IF EXISTS Inventory";

        public SiloDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_INVENTORY_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_INVENTORY_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}

