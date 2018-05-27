package com.vandenbreemen.sim_assistant.mvp.tts

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.vandenbreemen.sim_assistant.api.sim.Sim
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TTSInteractorTest {

    val sim = Sim(
            "Test Sim",
            "Kevin", System.currentTimeMillis(),
            "This is a test of a sim.  It has two sentences.  Hear it?"
    )

    @Test
    fun shouldSpeakASim() {
        //  Arrange
        val interactor = TTSInteractorImpl(InstrumentationRegistry.getTargetContext())

        //  Act
        val totalUtterancesAndObservable = interactor.speakSim(sim)

        //  Assert
        var done = false
        totalUtterancesAndObservable.second.subscribe({}, {}, {
            done = true
        })

        await().atMost(5, TimeUnit.SECONDS).until { done }
    }

}