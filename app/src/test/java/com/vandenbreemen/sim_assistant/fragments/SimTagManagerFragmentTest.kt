package com.vandenbreemen.sim_assistant.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.R.drawable.tag
import com.vandenbreemen.sim_assistant.R.drawable.tag_blue
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.SimTag
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.fragments.SimTagManagerFragment.Companion.ARG_SIM
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
class SimTagManagerFragmentTest {

    val app = RuntimeEnvironment.application as SimAssistantApp

    @Before
    fun setup() {
        ShadowLog.stream = System.out
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
    }

    @Test
    fun shouldShowTagsOnInitialLoad() {

        //  Arrange
        app.boxStore.boxFor(Tag::class.java).put(Tag(0, "Test", false))
        val sim = Sim(0, "Kevin", "Test", 0, "")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        //  Act
        val fragment = SimTagManagerFragment()
        val args = Bundle()
        args.putParcelable(ARG_SIM, sim)
        fragment.arguments = args

        startFragment(fragment)
        val tagsList = fragment.view!!.findViewById<RecyclerView>(R.id.tagSelector)
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)

        //  Assert
        assertEquals("Tag List", 1, tagsList.childCount)
        val item = tagsList.getChildAt(0) as ViewGroup
        assertEquals("Test", item.findViewById<TextView>(R.id.tagName).text)

        val imageIconButton = item.findViewById<ImageButton>(R.id.tagIconButton)
        val shadowDrawable = shadowOf(imageIconButton.drawable)
        assertEquals("Non-selected Tag", tag, shadowDrawable.createdFromResId)
    }

    @Test
    fun shouldDisplaySimAsTaggedOnInitialLoad() {
        //  Arrange
        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)
        val sim = Sim(0, "Kevin", "Test", 0, "")
        app.boxStore.boxFor(Sim::class.java).put(sim)
        app.boxStore.boxFor(SimTag::class.java).put(SimTag(0, sim.id, tag.id))

        //  Act
        val fragment = SimTagManagerFragment()
        val args = Bundle()
        args.putParcelable(ARG_SIM, sim)
        fragment.arguments = args

        startFragment(fragment)
        val tagsList = fragment.view!!.findViewById<RecyclerView>(R.id.tagSelector)
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)

        val item = tagsList.getChildAt(0) as ViewGroup
        val imageIconButton = item.findViewById<ImageButton>(R.id.tagIconButton)
        val shadowDrawable = shadowOf(imageIconButton.drawable)
        assertEquals("Selected Tag", tag_blue, shadowDrawable.createdFromResId)
    }

    @Test
    fun shouldAddTagToSim(){
        //  Arrange
        app.boxStore.boxFor(Tag::class.java).put(Tag(0, "Test", false))
        val sim = Sim(0, "Kevin", "Test", 0,"")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        //  Act
        val fragment = SimTagManagerFragment()
        val args = Bundle()
        args.putParcelable(ARG_SIM, sim)
        fragment.arguments = args

        startFragment(fragment)
        val tagsList = fragment.view!!.findViewById<RecyclerView>(R.id.tagSelector)
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)
        val item = tagsList.getChildAt(0) as ViewGroup
        item.findViewById<ImageButton>(R.id.tagIconButton).performClick()
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)

        //  Assert
        val imageIconButton = item.findViewById<ImageButton>(R.id.tagIconButton)
        val shadowDrawable = shadowOf(imageIconButton.drawable)
        assertEquals("Selected Tag", tag_blue, shadowDrawable.createdFromResId)

    }

    @Test
    fun shouldUnTagSim() {
        //  Arrange
        app.boxStore.boxFor(Tag::class.java).put(Tag(0, "Test", false))
        val sim = Sim(0, "Kevin", "Test", 0, "")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        //  Act
        val fragment = SimTagManagerFragment()
        val args = Bundle()
        args.putParcelable(ARG_SIM, sim)
        fragment.arguments = args

        startFragment(fragment)
        val tagsList = fragment.view!!.findViewById<RecyclerView>(R.id.tagSelector)
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)
        val item = tagsList.getChildAt(0) as ViewGroup
        item.findViewById<ImageButton>(R.id.tagIconButton).performClick()
        item.findViewById<ImageButton>(R.id.tagIconButton).performClick()
        tagsList.measure(0, 0)
        tagsList.layout(0, 0, 100, 10000)

        //  Assert
        val imageIconButton = item.findViewById<ImageButton>(R.id.tagIconButton)
        val shadowDrawable = shadowOf(imageIconButton.drawable)
        assertEquals("Selected Tag", tag, shadowDrawable.createdFromResId)
    }

}