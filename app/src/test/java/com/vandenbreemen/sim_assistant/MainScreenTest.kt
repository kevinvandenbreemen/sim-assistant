package com.vandenbreemen.sim_assistant

import android.view.ViewGroup
import android.widget.Button
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
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

}