package com.example.wingwatcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;
import android.widget.TextView;

public class birdInfoTest {

    @Rule
    public ActivityScenarioRule<birdInfo> birdInfoActivityRule = new ActivityScenarioRule<>(birdInfo.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void displayBirdInformation() {
        // Verify that the bird name and info are displayed
        onView(withId(R.id.birdName)).check(matches(isDisplayed()));
        onView(withId(R.id.birdInfo)).check(matches(isDisplayed()));
    }

    @Test
    public void loadImageCorrectly() {
        // Verify that the image is displayed
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void clickBackButton_NavigatesToDashboard() {
        // Click on the back button
        onView(withId(R.id.backButton)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(dashboard.class.getName()));
    }

    @Test
    public void checkEditableTextBoxVisibility() {
        int userPermission = userInfo.getPrivilege();
        if (userPermission != 0) {
            onView(withId(R.id.editableBirdInfo)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.editableBirdInfo)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void researcherButtonFunctionality() {
        onView(withId(R.id.researcher)).perform(click());
    }
    @Test
    public void testResearcherButtonWithInsufficientPrivilege() {
        // Capture the original text of the birdInfo TextView
        final String[] originalText = new String[1];
        onView(withId(R.id.birdInfo)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "capture text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                originalText[0] = tv.getText().toString();
            }
        });

        // Perform click on the researcher button
        onView(withId(R.id.researcher)).perform(click());

        // Check if the birdInfo text is temporarily changed
        onView(withId(R.id.birdInfo)).check(matches(withText("Incorrect Permission - This will reset in 3 seconds")));

        // Wait for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the text is reset to the original value
        onView(withId(R.id.birdInfo)).check(matches(withText(originalText[0])));
    }


}
