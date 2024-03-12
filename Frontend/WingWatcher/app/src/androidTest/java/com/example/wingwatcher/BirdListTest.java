package com.example.wingwatcher;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import androidx.test.espresso.contrib.RecyclerViewActions;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BirdListTest {

    @Rule
    public ActivityTestRule<birdList> activityTestRule = new ActivityTestRule<>(birdList.class);

    @Test
    public void testRecyclerViewVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testBackButtonNavigation() {
        Espresso.onView(ViewMatchers.withId(R.id.backButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.drawerlayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testSearchFunctionality() {
        // Type text in the searchEditText, click the first item, and check if it navigates to the details activity
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
                .perform(ViewActions.typeText("Sparrow"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
    }

}
