package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.post.SimRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagInteractor
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SimTagInteractorImplTest {

    lateinit var app: SimAssistantApp

    lateinit var interactor: SimTagInteractor

    lateinit var sim: Sim

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
        app = RuntimeEnvironment.application as SimAssistantApp
        interactor = SimTagInteractorImpl(TagRepositoryImpl(app), SimRepositoryImpl(app))
        sim = Sim(0, "Test Sim", "Kevin", 0, "Test Content")
        app.boxStore.boxFor(Sim::class.java).put(sim)
    }

    @Test
    fun shouldAddTagToSim() {
        //  Arrange
        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        interactor.addTag(sim, tag).subscribe()

        //  Assert
        val tags = interactor.getTags(sim).blockingGet()
        assertEquals("Single Tag", 1, tags.size)
        assertTrue("Tag Selected", tags[0].selected)
    }


}