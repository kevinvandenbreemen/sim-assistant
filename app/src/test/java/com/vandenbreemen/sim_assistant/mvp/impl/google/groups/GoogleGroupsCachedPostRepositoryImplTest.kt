package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsCachedPostRepository
import junit.framework.TestCase
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GoogleGroupsCachedPostRepositoryImplTest {

    companion object {
        const val SIM_URL = "https://groups.google.com/forum/print/msg/fake-grp/1234"
    }

    lateinit var googleGroupsCachedPostRepository: GoogleGroupsCachedPostRepository

    @Before
    fun setup() {
        googleGroupsCachedPostRepository = GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
    }

    @Test
    fun shouldRecognizeWhenNoContentCached() {
        val cachedSim = googleGroupsCachedPostRepository.retrieve(SIM_URL)
        assertNull(cachedSim)

    }

    @Test
    fun shouldGetCachedSim() {
        googleGroupsCachedPostRepository.cacheSim(SIM_URL, "There once was a man\nnamed Brian")
        val cachedSim = googleGroupsCachedPostRepository.retrieve(SIM_URL)
        assertNotNull(cachedSim)

        TestCase.assertEquals("Sim content", "There once was a man\nnamed Brian", cachedSim!!.content)
        TestCase.assertEquals("Sim URL", SIM_URL, cachedSim!!.key)

    }

    @Test
    fun shouldGenerateIdWhenCachingSim(){
        val cached = googleGroupsCachedPostRepository.cacheSim(SIM_URL, "There once was a man\nnamed Brian")
        assertEquals("Generated ID", 1L, cached.id)
    }

    @Test
    fun shouldMissOnNoSuchKey() {
        googleGroupsCachedPostRepository.cacheSim(SIM_URL, "There once was a man\nnamed Brian")
        assertNull("Incorrect key", googleGroupsCachedPostRepository.retrieve(SIM_URL + "bb"))
    }

}