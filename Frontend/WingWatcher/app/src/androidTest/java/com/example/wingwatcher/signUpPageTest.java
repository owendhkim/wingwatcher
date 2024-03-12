package com.example.wingwatcher;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.text.InputType;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class signUpPageTest {

    @Rule
    public ActivityTestRule<signUpPage> activityRule = new ActivityTestRule<>(signUpPage.class);

    @Test
    public void testRegistrationSuccess() {
        onView(withId(R.id.fullName)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("john.doe@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.loginPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationFailure() {
        // Test case for entering invalid registration data.
        onView(withId(R.id.fullName)).perform(typeText("Hello"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("short"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.email)).check(matches(hasErrorText("Please enter a valid email")));
    }

    @Test
    public void testPasswordMismatch() {
        // Test case for entering passwords that do not match.
        onView(withId(R.id.fullName)).perform(typeText("Hello"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("Hello"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.confirmPassword)).check(matches(hasErrorText("Passwords do not match")));
    }

    @Test
    public void testShortPassword() {
        onView(withId(R.id.fullName)).perform(typeText("Hello"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("s@s.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("short"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("short"), closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.password)).check(matches(hasErrorText("Password must be at least 6 characters long.")));
    }


}
