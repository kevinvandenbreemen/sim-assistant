package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.functions.Consumer
import org.awaitility.Awaitility.await
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class TTSInteractorImplTest{

    lateinit var ttsInteractor: TTSInteractor

    @Before
    fun setup(){
        ttsInteractor = TTSInteractorImpl(RuntimeEnvironment.application.applicationContext)
    }

    @Test
    fun shouldParseNumberOfUtterances(){
        //  Arrange
        val sim = Sim(
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        //  Assert
        assertEquals("Number of Utterances", 7, numOfUtterancesToProgressObservable.first)
    }

    @Test
    fun shouldSpeakMultipleSims(){
        //  Arrange
        val sim = Sim(
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim, sim))

        //  Assert
        assertEquals("Number of Utterances", 14, numOfUtterancesToProgressObservable.first)
    }

    @Test
    fun shouldSpeakSims() {
        //  Arrange
        val sim = Sim(
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        numOfUtterancesToProgressObservable.second.subscribe(Consumer {
            println("Visited $it")
            listOfIndexesVisited.add(it)
        })

        //  Assert
        await().atMost(5, TimeUnit.SECONDS).until { listOfIndexesVisited.size == 7 }
        assertEquals("All indexes", listOf(0, 1, 2, 3, 4, 5, 6), listOfIndexesVisited)
    }

    @Test
    @Config(shadows = [ShadowTTSExt::class])
    fun shouldPauseDictation() {
        //  Arrange
        val sim = Sim(
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived."
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        var wasPausedAlready = false
        numOfUtterancesToProgressObservable.second.subscribe(Consumer {
            if (it == 3 && !wasPausedAlready) {
                ttsInteractor.pause()
                wasPausedAlready = true
            }
            listOfIndexesVisited.add(it)
        })

        await().atMost(5, TimeUnit.SECONDS).until { listOf<Int>(0, 1, 2, 3) == listOfIndexesVisited }

        ttsInteractor.resume()

        await().atMost(5, TimeUnit.SECONDS).until { listOfIndexesVisited.size == 6 }
        assertEquals("Visited items", listOf(0, 1, 2, 3, 3, 4), listOfIndexesVisited)
    }

}