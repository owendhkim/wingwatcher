package com.example.wingwatcher;

import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class loginPageTest {

    @Rule
    public ActivityTestRule<loginPage> activityRule = new ActivityTestRule<>(loginPage.class);

    @Test
    public void testLoginSuccess() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawerlayout)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        onView(withId(R.id.email)).perform(typeText("ss@ss.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(ViewActions.click());
        try {
            Thread.sleep(2000); // Adjust the duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.email)).check(matches(hasErrorText("Invalid email or password")));

    }

    @Test
    public void testEmptyFields() {
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Please enter a valid email")));
    }

    @Test
    public void testPasswordVisibilityToggle() {

        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("ssssss"), closeSoftKeyboard());
        onView(withId(R.id.passHider)).perform(click());
        onView(withId(R.id.password)).check(matches(withText("••••••")));
        onView(withId(R.id.passHider)).perform(click());
        onView(withId(R.id.password)).check(matches(withText("ssssss")));
    }
    @Test
    public void testShortPassword() {
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("short"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Password must be greater than 6 characters")));
    }



}

