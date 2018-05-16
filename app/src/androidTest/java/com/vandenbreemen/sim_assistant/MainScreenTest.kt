package com.vandenbreemen.sim_assistant

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.vandenbreemen.sim_assistant.util.ElapsedTimeIdlingResource
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
        //  googleGroupDetails
        activityRule.launchActivity(null)

        onView(withText("Google Group")).perform(click())

        onView(withId(R.id.googleGroupDetails)).check(matches(isDisplayed()))

        writeTo(R.id.googleGroupName, "sb118-apollo")
        clickOn(R.id.ok)
    }

}