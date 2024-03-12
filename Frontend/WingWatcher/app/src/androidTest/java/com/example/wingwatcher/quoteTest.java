package com.example.wingwatcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.view.View;
import android.widget.TextView;

public class quoteTest {
    @Rule
    public ActivityScenarioRule<quoteGenerator> quoteGeneratorActivityRule = new ActivityScenarioRule<>(quoteGenerator.class);
    private String[] quotesArray;

    @Before
    public void setUp() {
        Intents.init();
        quoteGeneratorActivityRule.getScenario().onActivity(activity -> quotesArray = activity.getQuotesArray());
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testRandomQuoteGeneration() {
        onView(withId(R.id.quoteButton)).perform(click());
        onView(withId(R.id.quoteBox)).check(matches(withText(isOneOf(quotesArray))));
    }

    @Test
    public void testQuoteChangesOnButtonClick() {
        onView(withId(R.id.quoteButton)).perform(click());
        final String[] firstQuote = new String[1];
        onView(withId(R.id.quoteBox)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                firstQuote[0] = tv.getText().toString();
            }
        });

        onView(withId(R.id.quoteButton)).perform(click());
        onView(withId(R.id.quoteBox)).check(matches(not(withText(firstQuote[0]))));
    }

    @Test
    public void clickBackButton_NavigatesToDashboard() {
        onView(withId(R.id.backButton)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(dashboard.class.getName()));
    }
}
