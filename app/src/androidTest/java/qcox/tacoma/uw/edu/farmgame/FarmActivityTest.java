package qcox.tacoma.uw.edu.farmgame;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by Cox Family on 5/31/2016.
 */
public class FarmActivityTest extends
        ActivityInstrumentationTestCase2<FarmActivity> {

    private Solo solo;

    public FarmActivityTest() {
        super(FarmActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();

    }
}
