package com.example.wingwatcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class userManageTest {
    @Rule
    public ActivityScenarioRule<userManage> userManageActivityRule = new ActivityScenarioRule<>(userManage.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void clickBackButton_NavigatesToBirdAnalytics() {
        onView(withId(R.id.back)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(birdAnalytics.class.getName()));
    }

    @Test
    public void checkButtonsAreClickable() {
        onView(withId(R.id.displayChanges)).check(matches(isClickable()));
        onView(withId(R.id.submitChanges)).check(matches(isClickable()));
        onView(withId(R.id.selectUser)).check(matches(isClickable()));
    }

    @Test
    public void editTextForUsernameAcceptsInput() {
        onView(withId(R.id.editText1)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editText1)).check(matches(withText("test@example.com")));
    }

    @Test
    public void editTextForPasswordAcceptsInput() {
        onView(withId(R.id.editText2)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editText2)).check(matches(withText("password123")));
    }
}
