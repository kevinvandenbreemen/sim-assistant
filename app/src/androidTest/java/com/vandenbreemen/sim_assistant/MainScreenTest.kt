package com.vandenbreemen.sim_assistant

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers.isPlatformPopup
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.vandenbreemen.sim_assistant.adapters.SimViewHolder
import com.vandenbreemen.sim_assistant.util.ElapsedTimeIdlingResource
import com.vandenbreemen.sim_assistant.util.checkItemExists
import com.vandenbreemen.sim_assistant.util.clickItemMatching
import com.vandenbreemen.sim_assistant.util.hasItemCount
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    val elapsedTimeIdlingResource = ElapsedTimeIdlingResource(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS))

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(elapsedTimeIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(elapsedTimeIdlingResource)
    }

    @Test
    fun shouldPromptUserForSimSource(){

        activityRule.launchActivity(null)

        onView(withId(R.id.selectSimSource)).check(matches(isDisplayed()))

    }

    @Test
    fun shouldPromptUserForGoogleGroupDetailsWhenSheSelectsGoogleGroups() {
        activityRule.launchActivity(null)

        onView(withText("Google Group")).perform(click())

        onView(withId(R.id.googleGroupDetails)).check(matches(isDisplayed()))

        writeTo(R.id.googleGroupName, "sb118-apollo")
        clickOn(R.id.ok)
    }

    @Test
    fun shouldShowGoogleGroupContainerOnceGoogleGroupConfigured() {
        activityRule.launchActivity(null)

        onView(withText("Google Group")).perform(click())

        onView(withId(R.id.googleGroupDetails)).check(matches(isDisplayed()))

        writeTo(R.id.googleGroupName, "sb118-apollo")
        clickOn(R.id.ok)

        val waitLonger = ElapsedTimeIdlingResource(TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS))
        IdlingRegistry.getInstance().register(waitLonger)

        try {
            onView(withId(R.id.simListContainer)).check(matches(isDisplayed()))
        }
        finally{
            IdlingRegistry.getInstance().unregister(waitLonger)
        }
    }

    @Test
    fun shouldOpenSimContentByClickingOnItInSimList(){
        activityRule.launchActivity(null)

        onView(withText("Google Group")).perform(click())

        onView(withId(R.id.googleGroupDetails)).check(matches(isDisplayed()))

        writeTo(R.id.googleGroupName, "sb118-apollo")
        clickOn(R.id.ok)

        val waitLonger = ElapsedTimeIdlingResource(TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS))
        IdlingRegistry.getInstance().register(waitLonger)

        try {
            onView(withId(R.id.simList)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<SimViewHolder>(0, click())
            )
            onView(withId(R.id.simDisplayContent)).check(matches(isDisplayed()))
        }
        finally{
            IdlingRegistry.getInstance().unregister(waitLonger)
        }
    }

    @Test
    fun shouldOpenTagManagerOnSelectTags() {
        activityRule.launchActivity(null)

        onView(withText("Google Group")).perform(click())

        onView(withId(R.id.googleGroupDetails)).check(matches(isDisplayed()))

        writeTo(R.id.googleGroupName, "sb118-apollo")
        clickOn(R.id.ok)

        val waitLonger = ElapsedTimeIdlingResource(TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS))
        IdlingRegistry.getInstance().register(waitLonger)

        try {
            onView(withId(R.id.simList)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<SimViewHolder>(0, longClick())
            )
            onView(withId(R.id.simList)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<SimViewHolder>(0,
                            checkItemExists(withId(R.id.simMenu)))
            )

            onView(withId(R.id.simList)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<SimViewHolder>(0,
                            clickItemMatching(withId(R.id.simMenu)))
            )

            onView(withText("Tags")).inRoot(isPlatformPopup()).check(matches(
                    withEffectiveVisibility(Visibility.VISIBLE)
            ))

            Thread.sleep(200)

            onView(withText("Tags")).inRoot(isPlatformPopup()).perform(click())

            //  tagAddSelect
            onView(withId(R.id.tagAddSelect)).check(matches(isDisplayed()))

            onView(withId(R.id.addInput)).perform(typeText("New Tag"))

            onView(withId(R.id.addTag)).perform(click())

            //  Verify tag now present
            onView(withId(R.id.tagSelector)).check(hasItemCount(1))

        }
        finally{
            IdlingRegistry.getInstance().unregister(waitLonger)
        }
    }

}