package qcox.tacoma.uw.edu.farmgame;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;

/**
 * This is a test class that test the PlayerValues class.
 * <p>
 * I chose to test this modal class (PlayerValues) because it is so important to our game functioning properly.
 * This class (the class we are testing) holds the game state of the game (besides the fields) so
 * it is constantly queried and saved to and must be solid as far as functionality.
 * <p>
 * Created by Cox Family on 5/31/2016.
 */
public class PlayerValuesTest extends TestCase {

    private static String INITIAL_USER_NAME = "billy@go.com";
    private static int INITIAL_MONEY = 20;
    private static int INITIAL_LEVEL = 2;
    private static int INITIAL_EXP = 5;
    private static String INITIAL_PLANT = "rhubarb";
    private static int INITIAL_PLANT_AMOUNT = 3;
    /**
     * The player values for the test.
     */
    PlayerValues mPlayerValues;
    /**
     * The item map for the test.
     */
    Map<String, Integer> mItemMap;

    /**
     * Instantiates a new Player values test.
     */
    public PlayerValuesTest() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        int money = INITIAL_MONEY;
        int level = INITIAL_LEVEL;
        int exp = INITIAL_EXP;
        mItemMap = new HashMap<>();
        mPlayerValues = new PlayerValues(INITIAL_USER_NAME, money, level, exp, mItemMap);
        mItemMap.put(INITIAL_PLANT, INITIAL_PLANT_AMOUNT);
    }

    /**
     * Test constructor.
     */
    @Test
    void TestConstructor(){
        assertNotNull("Constructor failed", mPlayerValues);
    }

    /**
     * Test set null item name.
     */
    @Test
    public void testSetNullItemName() {
        try {
            mPlayerValues.setItemAmount(null, 1);
            fail("Item can be set to null");
        }
        catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test set item amount to negative.
     */
    @Test
    public void testSetItemAmountToNegative() {
        try {
            mPlayerValues.setItemAmount(INITIAL_PLANT, -20);
            fail("Item can be set to negative amount");
        }
        catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test get user name.
     */
    @Test
    public void testGetUserName() {
        assertEquals(INITIAL_USER_NAME, mPlayerValues.getUserName());
    }

    /**
     * Test set user name.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetUserName() throws Exception {
        String newUserName = "Stacy";
        mPlayerValues.setUserName(newUserName);
        assertEquals(newUserName, mPlayerValues.getUserName());
    }

    /**
     * Test get money.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetMoney() throws Exception {
        assertEquals(INITIAL_MONEY, mPlayerValues.getMoney());
    }

    /**
     * Test add money.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAddMoney() throws Exception {
        int moneyToAdd = 50;
        mPlayerValues.addMoney(moneyToAdd);
        assertEquals((INITIAL_MONEY + moneyToAdd), mPlayerValues.getMoney());
    }

    /**
     * Test set money.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetMoney() throws Exception {
        int newMoneyAmount = 30;
        mPlayerValues.setMoney(newMoneyAmount);
        assertEquals(newMoneyAmount, mPlayerValues.getMoney());
    }

    /**
     * Test get level.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetLevel() throws Exception {
        assertEquals(INITIAL_LEVEL, mPlayerValues.getLevel());
    }

    /**
     * Test add one level.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAddOneLevel() throws Exception {
        mPlayerValues.addOneLevel();
        assertEquals((INITIAL_LEVEL + 1), mPlayerValues.getLevel());
    }

    /**
     * Test set level.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetLevel() throws Exception {
        int newLevel = 3;
        mPlayerValues.setLevel(newLevel);
        assertEquals(newLevel, mPlayerValues.getLevel());
    }

    /**
     * Test get exp.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetExp() throws Exception {
        assertEquals(INITIAL_EXP, mPlayerValues.getExp());
    }

    /**
     * Test add exp.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAddExp() throws Exception {
        int newExp = 1;
        mPlayerValues.addExp(newExp);
        assertEquals((INITIAL_EXP + newExp), mPlayerValues.getExp());
    }

    /**
     * Test set exp.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetExp() throws Exception {
        int newExp = 6;
        mPlayerValues.setExp(newExp);
        assertEquals((newExp), mPlayerValues.getExp());
    }

    /**
     * Test get item amount.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetItemAmount() throws Exception {
        assertEquals(INITIAL_PLANT_AMOUNT, mPlayerValues.getItemAmount(INITIAL_PLANT));
    }

    /**
     * Test add item amount.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAddItemAmount() throws Exception {
        int amountToAdd = 3;
        mPlayerValues.addItemAmount(INITIAL_PLANT, amountToAdd);
        assertEquals((INITIAL_PLANT_AMOUNT + amountToAdd), mPlayerValues.getItemAmount(INITIAL_PLANT));
    }

    /**
     * Test set item amount.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetItemAmount() throws Exception {
        int newAmount = 8;
        mPlayerValues.setItemAmount(INITIAL_PLANT, newAmount);
        assertEquals(newAmount, mPlayerValues.getItemAmount(INITIAL_PLANT));
    }

    /**
     * Test get item map.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetItemMap() throws Exception {
        assertEquals(mItemMap, mPlayerValues.getItemMap());
    }

//    @Test
//    public void testSetItemMapToNull() throws Exception {
//        Map<String, Integer> newItemMap = new HashMap<>();
//        newItemMap.put("weeds", 1000000000);
//        mPlayerValues.setItemMap(newItemMap);
//        assertEquals(newItemMap, mPlayerValues.getItemMap());
//    }

    /**
     * Test set item map.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetItemMap() throws Exception {
        Map<String, Integer> newItemMap = new HashMap<>();
        newItemMap.put("weeds", 1000000000);
        mPlayerValues.setItemMap(newItemMap);
        assertEquals(newItemMap, mPlayerValues.getItemMap());
    }
}