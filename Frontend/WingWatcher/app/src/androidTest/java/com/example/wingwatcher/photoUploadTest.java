package com.example.wingwatcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
public class photoUploadTest {
    @Rule
    public ActivityScenarioRule<photoUpload> photoUploadActivityRule = new ActivityScenarioRule<>(photoUpload.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testImageViewVisibility() {
        // Check if imageView is visible
        // This is a basic check and might need to be adjusted based on the initial state of imageView in your activity
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void testUploadButtonClick() {
        // Click the upload button
        onView(withId(R.id.uploadButton)).perform(click());

        // This test might be limited since we can't verify the actual upload without mocking
        // Additional checks can be added if there are any UI changes upon button click
    }

    @Test
    public void clickBackButton_NavigatesToBirdAnalytics() {
        onView(withId(R.id.back)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(birdAnalytics.class.getName()));
    }
}
