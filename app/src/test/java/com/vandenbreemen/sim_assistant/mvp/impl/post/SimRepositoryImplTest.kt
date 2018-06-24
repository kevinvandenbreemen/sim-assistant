package com.vandenbreemen.sim_assistant.mvp.impl.post

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.post.SimRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class SimRepositoryImplTest{

    lateinit var simRepository: SimRepository

    @Before
    fun setup(){
        simRepository = SimRepositoryImpl(
                RuntimeEnvironment.application as SimAssistantApp
        )
    }

    @Test
    fun shouldStoreSim(){
        val sim = Sim(
                0,
                "test",
                "kevin",
                12,
                "Test Content"
        )

        simRepository.store(sim).blockingAwait()

        val retrieved = simRepository.load(1L).blockingGet()
        assertEquals("Title", "test", retrieved.title)
        assertEquals("Author", "kevin", retrieved.author)
        assertEquals("Post Date", 12L, retrieved.postedDate)
        assertEquals("Content", "Test Content", retrieved.content)
    }

}