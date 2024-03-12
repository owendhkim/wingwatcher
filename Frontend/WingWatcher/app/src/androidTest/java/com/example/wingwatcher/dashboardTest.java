package com.example.wingwatcher;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.core.StringContains.containsString;

public class dashboardTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    //This is the code for adding time to day by 2 seconds if needed use in down
//    try {
//        Thread.sleep(2000); // Adjust the duration as needed
//    } catch (InterruptedException e) {
//        e.printStackTrace();
//    }


    @Rule
    public ActivityScenarioRule<loginPage> loginPageActivityRule = new ActivityScenarioRule<>(loginPage.class);

    @Test
    public void displayUsernameOnCreate() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        onView(withId(R.id.username)).check(matches(withText(userInfo.getUsername())));
    }

    @Test
    public void clickProfileImage_NavigatesToProfilePage() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        // Click on the profile image
        onView(withId(R.id.profileImage)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(profilePage.class.getName()));
    }

    @Test
    public void clickUserAnalyticsCard_AdminNavigatesToBirdAnalytics() {
        onView(withId(R.id.email)).perform(typeText("world"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("helloworld"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        onView(withId(R.id.userAnalyticsIcon)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(birdAnalytics.class.getName()));
    }

    @Test
    public void clickUserAnalyticsCard_NonAdminShowsAccessErrorDialog() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        onView(withId(R.id.userAnalyticsIcon)).perform(click());
        onView(withText(containsString("We are sorry, this page is only accessible by admins and researchers.")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickCameraCard_NavigatesToCamera() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.cameraIcon)).perform(click());
        // Check if the intent to MainActivity is triggered
        Intents.intended(IntentMatchers.hasComponent(camera.class.getName()));
    }

    @Test
    public void clickPhotoUploadCard_NavigatesToPhotoUpload() {
        // Click on the Photo Upload card
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.photoUploadIcon)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(photoUpload.class.getName()));
    }


    @Test
    public void clickBirdListCard_NavigatesToBirdList() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        // Click on the Bird List card
        onView(withId(R.id.birdListIcon)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(birdList.class.getName()));
    }

    @Test
    public void clickMapPageCard_NavigatesToMapPage() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        // Click on the Map Page card
        onView(withId(R.id.mapPageIcon)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(mapPage.class.getName()));
    }

    @Test
    public void clickQuoteCard_NavigatesToQuoteGenerator() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.quoteIcon)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(quoteGenerator.class.getName()));
    }

}
