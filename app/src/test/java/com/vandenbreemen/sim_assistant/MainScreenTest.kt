package com.vandenbreemen.sim_assistant

import android.arch.persistence.room.Room
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.DATABASE_NAME
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class MainScreenFunctionalTest {

    @get:Rule
    val errorCollector = ErrorCollector()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
    }

    @Test
    fun shouldAllowUserToPickGoogleGroups() {
        val mainActivity = buildActivity(MainActivity::class.java)
                .create()
                .resume()
                .get()

        await().atMost(5, TimeUnit.SECONDS).until {
            mainActivity.findViewById<ViewGroup>(R.id.selectSimSource) != null
        }

        val simSourceSelector = mainActivity.findViewById<ViewGroup>(R.id.selectSimSource)
        val button = simSourceSelector.findViewWithTag<Button>(SimSource.GOOGLE_GROUP.name)

        assertNotNull("Button", button)
        errorCollector.checkThat(button.text.toString(), `is`("Google Group"))
    }

    @Test
    fun shouldAcceptGoogleGroupDetailsAndConfigureUserSettingsForGoogleGroups() {
        val mainActivity = buildActivity(MainActivity::class.java)
                .create()
                .resume()
                .get()

        await().atMost(5, TimeUnit.SECONDS).until {
            mainActivity.findViewById<ViewGroup>(R.id.selectSimSource) != null
        }

        val simSourceSelector = mainActivity.findViewById<ViewGroup>(R.id.selectSimSource)
        val button = simSourceSelector.findViewWithTag<Button>(SimSource.GOOGLE_GROUP.name)

        button.performClick()

        mainActivity.findViewById<EditText>(R.id.googleGroupName).setText("sb118-apollo")
        mainActivity.findViewById<Button>(R.id.ok).performClick()

        val database = Room.databaseBuilder(RuntimeEnvironment.application.applicationContext,
                AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .build()

        val userSettings = database.dao().getUserSettings()

        assertEquals("Data source", SimSource.GOOGLE_GROUP.getId(), userSettings!!.dataSource)

    }

}