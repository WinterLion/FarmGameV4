package qcox.tacoma.uw.edu.farmgame;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.Random;

/**
 *
 *
 * Created by Cox Family on 5/31/2016.
 */
public class RobotiumTest extends
        ActivityInstrumentationTestCase2<FarmActivity> {

    private Solo solo;
    private String mUsername;
    private String mPassword;

    /**
     * Instantiates a new Robotium test.
     */
    public RobotiumTest() {
        super(FarmActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        mUsername = "robotiumTest@gmail.com";
        mPassword = "123456";

    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    private void setUpLoginPage(){
        //make sure we are logged out (we may be logged in due to shared preferences
        solo.clickOnView(getActivity().findViewById(R.id.action_logout));
    }

    private void setUpMainPage(){
        setUpLoginPage();
        solo.enterText(0, mUsername);
        solo.enterText(1, mPassword);
        solo.clickOnButton("Log In");
    }

    public void testAppStarted() {
        setUpLoginPage();
        boolean activityLoaded = solo.searchText("Don't have an account?");
        assertTrue("Login activity loaded", activityLoaded);
    }

    public void testNavigateToRegistration() {
        setUpLoginPage();
        solo.clickOnButton("Go to Register");
        boolean worked = solo.searchText("Back to Login");
        assertTrue("Navigate To Registration worked!", worked);
    }

    public void testRegistration() {
        setUpLoginPage();
        solo.clickOnButton("Go to Register");
        Random random = new Random();
        //Generate a random user and password
        String user = "RobotiumTest" + (random.nextInt(400) + 1)
                + "@" + (random.nextInt(400) + 1) + "." + (random.nextInt(400) + 1);
        String password = "RobotiumTest";
        mUsername = user;
        mPassword = password;
        solo.enterText(0, mUsername);
        solo.enterText(1, mPassword);
        solo.clickOnButton("Register");
        boolean worked = solo.searchText("Don't have an account?");
        assertTrue("Registration worked!", worked);
        solo.enterText(0, mUsername);
        solo.enterText(1, mPassword);
        solo.clickOnButton("Log In");
        worked = solo.searchText("HighScore");
        assertTrue("Sign in after registration worked!", worked);
    }

    public void testLogin(){
        setUpLoginPage();
        solo.enterText(0, mUsername);
        solo.enterText(1, mPassword);
        solo.clickOnButton("Log In");
        boolean worked = solo.searchText("HighScore");
        assertTrue("Sign in worked!", worked);
    }

    public void testSilo(){
        setUpMainPage();
        solo.clickOnButton("Silo/Shop");
        boolean worked = solo.searchText("Strawberry");
        assertTrue("Navigate to Silo worked!", worked);
    }

    public void testItemDetail(){
        setUpMainPage();
        solo.clickOnButton("Silo/Shop");
        solo.clickInRecyclerView(1);
        boolean worked = solo.searchText("Item Details:");
        assertTrue("Item details worked!", worked);
    }

    public void testBackToSilo(){
        setUpMainPage();
        solo.clickOnButton("Silo/Shop");
        solo.clickInRecyclerView(1);
        solo.goBack();
        boolean worked = solo.searchText("Strawberry");
        assertTrue("Navigate back to Silo worked!", worked);
    }

    public void testBackToMain(){
        setUpMainPage();
        solo.clickOnButton("Silo/Shop");
        solo.clickInRecyclerView(1);
        solo.goBack();
        solo.goBack();
        boolean worked = solo.searchText("HighScore");
        assertTrue("from silo back to main worked!", worked);
    }

    public void testHelp(){
        setUpMainPage();
        solo.clickOnButton("Help");
        boolean worked = solo.searchText("Farm Game Tutorial");
        assertTrue("Help worked!", worked);
        solo.goBack();
    }

    public void testHighScore(){
        setUpMainPage();
        solo.clickOnButton("HighScore");
        boolean worked = solo.searchText("haha@mail.com");
        assertTrue("HighScore worked!", worked);
        solo.goBack();
    }

    public void testLogout(){
        setUpMainPage();
       // solo.clickOnButton("log out");
        solo.clickOnMenuItem("log out");
        solo.enterText(0, mUsername);
        solo.enterText(1, mPassword);
        solo.clickOnButton("Log In");
        boolean worked = solo.searchText("HighScore");
        assertTrue("Logout and Sign in worked!", worked);
    }

    public void testEmailFriend(){
        setUpMainPage();
        solo.clickOnMenuItem("email friends");
        //solo.clickOnButton("email friends");
        boolean worked = solo.searchText("You can share your game score");
        assertTrue("Email Friend navigation worked!", worked);
        solo.clickOnButton("not now");
    }
}
