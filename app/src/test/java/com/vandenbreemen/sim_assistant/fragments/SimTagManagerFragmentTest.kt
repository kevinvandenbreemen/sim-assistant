package com.vandenbreemen.sim_assistant.fragments

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
class SimTagManagerFragmentTest {

    val app = RuntimeEnvironment.application as SimAssistantApp

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
    }

    @Test
    fun shouldShowTagsOnInitialLoad() {

        //  Arrange
        app.boxStore.boxFor(Tag::class.java).put(Tag(0, "Test"))

        //  Act
        val fragment = SimTagManagerFragment()
        startFragment(fragment)
        val tagsList = fragment.view!!.findViewById<RecyclerView>(R.id.tagSelector)
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)

        //  Assert
        assertEquals("Tag List", 1, tagsList.childCount)
        val item = tagsList.getChildAt(0) as ViewGroup
        assertEquals("Test", item.findViewById<TextView>(R.id.tagName).text)

    }

}