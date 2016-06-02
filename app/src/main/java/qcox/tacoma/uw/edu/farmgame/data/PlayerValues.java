package qcox.tacoma.uw.edu.farmgame.data;

import java.util.HashMap;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.Config;

/**
 * This class holds some of the values that the player will need to play the game.
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class PlayerValues {
    //this is the money the player has
    private int mMoney;
    //this is used with the database to id the user
    public String mUserName;
    //this is the current level of the player
    private int mLevel;
    //this is the current Exp of the player
    public int mExp;
    //this is the current Score of the player
    public int mScore;

    //these are the amounts of each item the player has
    public  Map<String, Integer> mItemMap;

    public PlayerValues(String theUserName) {
        mUserName = theUserName;
        mMoney = Config.INITIAL_MONEY;
        mLevel = Config.INITIAL_LEVEL;
        mExp = Config.INITIAL_EXPERIENCE;
        mScore = Config.INITIAL_SCORE;
        mItemMap = new HashMap<>();
        for (int i = 0; i < Config.INITIAL_PLANT_NAMES.length; i++) {
            mItemMap.put(Config.INITIAL_PLANT_NAMES[i], Config.INITIAL_PLANT_AMOUNTS[i]);
        }
    }


    public PlayerValues(String theUserName, int theMoney, int theLevel, int theExp, Map<String, Integer> theItemMap) {
        mUserName = theUserName;
        mMoney = theMoney;
        mLevel = theLevel;
        mExp = theExp;
        //mScore = theScore;
        mItemMap = theItemMap;
    }

    public PlayerValues(String theUserName, int theMoney, int theLevel, int theExp, int theScore, Map<String, Integer> theItemMap) {
        mUserName = theUserName;
        mMoney = theMoney;
        mLevel = theLevel;
        mExp = theExp;
        mScore = theScore;
        mItemMap = theItemMap;
    }

    public  String getUserName() {
        return mUserName;
    }

    public  void setUserName(String UserName) {
        mUserName = UserName;
    }

    public  int getMoney() {
        return mMoney;
    }

    public  void addMoney(int Money) {
        mMoney += Money;
    }

    public  void setMoney(int Money) {
        mMoney = Money;
    }

    public  int getLevel() {
        return mLevel;
    }

    public  void addLevel() {
        mLevel ++;
    }

    public  void setLevel(int Level) {
        mLevel = Level;
    }

    public  int getExp() {
        return mExp;
    }

    public  void addExp(int Exp) {
        mExp += Exp;
    }

    public  void setExp(int Exp) {
        mExp = Exp;
    }

    public  int getItemAmount(String theItem) {
        int answer = 0;
        if (mItemMap.containsKey(theItem)) {
            answer = mItemMap.get(theItem);
        }
        return answer;
    }

    public  void addItemAmount(String theItem, int theAddition) {
        int amount = theAddition;
        if (mItemMap.containsKey(theItem)) {
            amount = mItemMap.get(theItem) + theAddition;
            if (amount <=0) {
                amount = 0;
            }
        }
        mItemMap.put(theItem, amount);
    }

    public  void setItemAmount(String theItem, int theNewAmount) {
        mItemMap.put(theItem, theNewAmount);
    }


    public  Map<String, Integer> getItemMap() {return mItemMap;}

    public  void setItemMap(Map<String, Integer> ItemMap) {
        mItemMap = ItemMap;
    }
}
