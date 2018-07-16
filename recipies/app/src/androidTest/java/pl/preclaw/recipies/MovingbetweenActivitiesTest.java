package pl.preclaw.recipies;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MovingbetweenActivitiesTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);
    private static String RECIPE = "Brownies";
    private static String INTRO = "Recipe Introduction";
    private static String RECIPE2 = "Nutella Pie";

    @Test
    public void checkIfPreviousAndNextButtonExist()  {

        onView(withText(RECIPE)).perform(click());
        onView(withText(INTRO)).perform(click());
        onView(withId(R.id.descr_previous)).check(matches(isDisplayed()));
        onView(withId(R.id.descr_next)).check(matches(isDisplayed()));

   }
    @Test
    public void checkIfPreviousAndNextButtonWorks()  {

        onView(withText(RECIPE2)).perform(click());
        onView(withText(INTRO)).perform(click());
        for(int i=0;i<20;i++){
            onView(withId(R.id.descr_previous)).perform(click());
        }
        for(int i=0;i<40;i++) {

            onView(withId(R.id.descr_next)).perform(click());
        }
    }
}
