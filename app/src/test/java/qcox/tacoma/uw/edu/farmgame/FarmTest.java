package qcox.tacoma.uw.edu.farmgame;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;

/**
 * Created by Cox Family on 5/31/2016.
 */
public class FarmTest extends TestCase {

    PlayerValues mPlayerValues;
    private static String INITIAL_USER_NAME = "billy@go.com";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        int money = 0;
        int level = 0;
        int exp = 0;
        Map<String, Integer> ItemMap = new HashMap<>();


        mPlayerValues = new PlayerValues(INITIAL_USER_NAME, money, level, exp, ItemMap);
    }

    @Test
    void TestConstructor(){
        assertNotNull("Constructor failed", mPlayerValues);
    }

    @Test
    public void testSetNullItemAmount() {
        try {
            mPlayerValues.setItemAmount(null, 1);
            fail("Item can be set to null");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetItemAmountToNegative() {
        try {
            mPlayerValues.setItemAmount("Potato", -20);
            fail("Item can be set to negative amount");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetUserName() {
        assertEquals(INITIAL_USER_NAME, mPlayerValues.getUserName());
    }


}
