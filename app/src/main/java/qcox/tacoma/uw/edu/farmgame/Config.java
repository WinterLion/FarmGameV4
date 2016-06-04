package qcox.tacoma.uw.edu.farmgame;

import java.util.Arrays;
import java.util.List;

/**
 * This is used to save some configuration, and the initial states of farm
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class Config {

    final public static int INITIAL_MONEY = 0; //initial money the player has
    final public static int INITIAL_EXPERIENCE = 0; //initial experience the player has
    final public static int INITIAL_LEVEL = 0; //initial level the player has
    final public static int INITIAL_SCORE = 0; //initial level the player has


    final public static int INITIAL_FIELD = 3;//initial field the player has
    final public static int LEVELUP_FIELD_GAP = 1;//number of field player get reward when level up
    final public static int LEVELUP_EXPERIENCE_REQUIRED = 10; //the experience player need to level up

    final public static int MAX_RETURN_ON_HARVEST = 3;
    final public static int MIN_RETURN_ON_HARVEST = 1;

    final public static String FARM_GAME_LOGCAT = "Farm Game";

    final public static int WHEATMUTURETIME = 4001; //wheat muture time.
    final public static int CORNMUTURETIME = 6001;//corn muture time
    final public static int POTATOMUTURETIME = 10001;//potato muture time
    final public static int STRAWBERRYMUTURETIME = 20001;//strawberry muture time
//    final public static int WHEATEXP = 1;//wheat experience when harvest one
//    final public static int CORNEXP = 2;//corn experience when harvest one
//    final public static int POTATOEXP = 3;//potato experience when harvest one
//    final public static int STRAWBERRYEXP = 4;//strawberry experience when harvest one
//    final public static int WHEATMONEY = 1;//wheat money when harvest one
//    final public static int CORNMONEY = 2;//corn money when harvest one
//    final public static int POTATOMONEY = 3;//potato money when harvest one
//    final public static int STRAWBERRYMONEY = 4;//strawberry money when harvest one

    final public static String WHEAT = "Wheat";
    final public static String CORN = "Corn";
    final public static String POTATO = "Potato";
    final public static String STRAWBERRY = "Strawberry";
    final public static String FIELD = "field";
//    final public static String[] INITIAL_PLANT_NAMES = {"Potato", "Corn", "Wheat", "Strawberry"};
//    final public static int[] INITIAL_PLANT_AMOUNTS = {5, 5, 5, 5};
    final public static String[] INITIAL_PLANT_NAMES = {"Wheat"};//initial crops the player log in for the first time
    final public static int[] INITIAL_PLANT_AMOUNTS = {1};//initial amount of the cooresponding crops the player log in for the first time

}
