package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

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
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSim(sim)

        //  Assert
        assertEquals("Number of Utterances", 7, numOfUtterancesToProgressObservable.first)
    }

}