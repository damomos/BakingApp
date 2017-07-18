package com.example.princess.bakingapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.princess.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Princess on 7/18/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest(){
        ViewInteraction recyclerView = onView(allOf(withId(R.id.recipe_list), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView2 = onView(allOf(withId(R.id.ingredients_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1,click()));

        ViewInteraction recyclerView3 = onView(allOf(withId(R.id.steps_list), isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(1,click()));
    }


}
