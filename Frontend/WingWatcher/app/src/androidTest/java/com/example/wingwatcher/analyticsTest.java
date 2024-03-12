package com.example.wingwatcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class analyticsTest {
    @Rule
    public ActivityScenarioRule<birdAnalytics> birdAnalyticsActivityRule = new ActivityScenarioRule<>(birdAnalytics.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testDefaultTextValues() {
        onView(withId(R.id.textView)).check(matches(withText("User Analytics")));
        onView(withId(R.id.numUsers)).check(matches(withText("hello there")));
        // If the other TextViews have IDs, add checks for them here
        // Example: onView(withId(R.id.connectionStatus)).check(matches(withText("Active")));
    }

    @Test
    public void testRefreshButtonBehavior() {
        onView(withId(R.id.refresh)).perform(click());
        onView(withId(R.id.numUsers)).check(matches(withText("hello there")));
        // Repeat the checks for other TextViews as in the first test
    }

    @Test
    public void clickUserManagement_NavigatesToUserManage() {
        onView(withId(R.id.userManagement)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(userManage.class.getName()));
    }
}

