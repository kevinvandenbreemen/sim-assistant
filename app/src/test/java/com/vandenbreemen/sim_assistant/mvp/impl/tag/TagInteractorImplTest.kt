package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.tag.TagInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TagInteractorImplTest {

    lateinit var app: SimAssistantApp

    lateinit var sim: Sim

    lateinit var tagInteractor: TagInteractor

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { AndroidSchedulers.mainThread() }
        app = RuntimeEnvironment.application as SimAssistantApp
        sim = Sim(0, "Test Sim", "Kevin", 0, "Test Content")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        this.tagInteractor = TagInteractorImpl(TagRepositoryImpl(app))
    }

    @Test
    fun shouldSearchTags() {
        //  Arrange
        val tag = Tag(0, "Test Tag")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        val tags = tagInteractor.searchTags("Test Tag").blockingGet()

        //  Assert
        assertEquals("Single Tag", 1, tags.size)
        assertEquals("Tag", tags[0], tag)
    }

}