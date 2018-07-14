package pl.preclaw.recipies;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);
    private static String RECIPE = "Brownies";
    private static String RECIPE2 = "Nutella Pie";
    private static String INTRO = "Recipe Introduction";
    private static String INGREDIENT = "5.0 unit of large eggs";




    private IdlingResource mIdlingResource;


    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecycleViewItemWithBackButton_OpensOrderActivity()  {
        onView(withText(RECIPE2)).perform(click());
//        onView(withId(R.id.steps_rv)).check(matches(withText(STEP)));
        String middleElementText =
                activityTestRule.getActivity().getResources()
                        .getString(R.string.ingredients);
        onView(withText(middleElementText)).check(matches(isDisplayed()));
        onView(withText(INTRO)).check(matches(isDisplayed()));



    }
    @Test
    public void clickRecycleViewItemWithCheckIngredient_OpensOrderActivity() {
        onView(withText(RECIPE)).perform(click());
        onView(withText(INGREDIENT)).check(matches(isDisplayed()));


    }
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
