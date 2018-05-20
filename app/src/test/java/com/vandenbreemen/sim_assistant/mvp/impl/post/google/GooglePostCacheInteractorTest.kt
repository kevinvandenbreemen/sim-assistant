package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.sim.CachedSim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class GooglePostCacheInteractorTest {

    companion object {
        const val SIM_URL = "https://groups.google.com/forum/print/msg/uss-odyssey-oe/43As_Cm9puU/ocvmDu--AgAJ"
    }

    lateinit var googlePostCacheInteractor: GooglePostCacheInteractor

    @Before
    fun setup(){
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
        googlePostCacheInteractor = GooglePostCacheInteractor(RuntimeEnvironment.application as SimAssistantApp)
    }

    @Test
    fun shouldRecognizeWhenNoContentCached(){
        val cachedSim = googlePostCacheInteractor.retrieve(SIM_URL)
        Assert.assertEquals("Not cached", GooglePostCacheInteractor.NO_CACHE_HIT, cachedSim)

    }

    @Test
    fun shouldGetCachedSim(){
        googlePostCacheInteractor.cacheSim(SIM_URL, "There once was a man\nnamed Brian")
        val cachedSim = googlePostCacheInteractor.retrieve(SIM_URL)

        assertEquals("Sim content", "There once was a man\nnamed Brian", cachedSim.content)
        assertEquals("Sim URL", SIM_URL, cachedSim.key)

    }

}