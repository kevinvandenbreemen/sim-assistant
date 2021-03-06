package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.tts.ShadowTTSExt.Companion.DEFAULT_SIMULATED_TTS_UTTERANCE_DURATION
import io.reactivex.functions.Consumer
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowTTSExt::class])
class TTSInteractorImplTest{

    lateinit var ttsInteractor: TTSInteractor

    @Before
    fun setup(){
        ShadowLog.stream = System.out
        ttsInteractor = TTSInteractorImpl(RuntimeEnvironment.application.applicationContext)
        simulatedTextToSpeechUtteranceDuration = DEFAULT_SIMULATED_TTS_UTTERANCE_DURATION
    }

    @After
    fun tearDown(){
        ttsInteractor.close()
    }


    @Test
    fun shouldParseNumberOfUtterances(){
        //  Arrange
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        //  Assert
        val simDictationDetails:SimDictationDetails = numOfUtterancesToProgressObservable.first
        assertEquals("Number of Utterances", 7, simDictationDetails.numberOfSentences)
    }

    @Test
    fun shouldSpeakMultipleSims(){
        //  Arrange
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim, sim))

        //  Assert
        val simDictationDetails:SimDictationDetails = numOfUtterancesToProgressObservable.first
        assertEquals("Number of Utterances", 14, simDictationDetails.numberOfSentences)
    }

    @Test
    fun shouldProvidePositionsForBeginningsOfEachSim(){
        //  Arrange
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        val sim1 = Sim(0L,
                "Test Sim Again",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim, sim1))

        //  Assert
        val simDictationDetails:SimDictationDetails = numOfUtterancesToProgressObservable.first
        assertEquals("Start Position Count", 2, simDictationDetails.simPositions.size)
        assertEquals("First Sim", 0, simDictationDetails.simPositions[sim])
        assertEquals("Second Sim", 7, simDictationDetails.simPositions[sim1])
    }

    @Test
    fun shouldSpeakSims() {
        //  Arrange
        simulatedTextToSpeechUtteranceDuration = 100L
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        var done = false
        numOfUtterancesToProgressObservable.second.subscribe( {
            println("Visited $it")
            listOfIndexesVisited.add(it)
        }, {}, {done=true})

        //  Assert
        await().atMost(5, TimeUnit.SECONDS).until { done }
        assertEquals("All indexes", listOf(0, 1, 2, 3, 4, 5, 6), listOfIndexesVisited)
    }

    @Test
    fun shouldIndicateSpeakingSimsWhenSpeakingSims() {
        //  Arrange
        simulatedTextToSpeechUtteranceDuration = 100L
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        numOfUtterancesToProgressObservable.second.subscribe({
            assertTrue("Speaking Sims", ttsInteractor.isInProcessOfSpeakingSims())
        }, {}, { assertFalse("Speaking Sims", ttsInteractor.isInProcessOfSpeakingSims()) })

        //  Assert
        await().atMost(5, TimeUnit.SECONDS).until { !ttsInteractor.isInProcessOfSpeakingSims() }
    }

    @Test
    fun shouldBeUsableAfterCompletingDictationOfSims(){
        //  Arrange
        simulatedTextToSpeechUtteranceDuration = 100L
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val listOfIndexesVisited = mutableListOf<Int>()

        var numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))
        var done = false
        numOfUtterancesToProgressObservable.second.subscribe( {
            listOfIndexesVisited.add(it)
        }, {}, {done=true})

        await().atMost(5, TimeUnit.SECONDS).until { done }

        println("Trying to re-use interactor")

        numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))
        done = false
        numOfUtterancesToProgressObservable.second.subscribe( {
            listOfIndexesVisited.add(it)
        }, {}, {done=true})

        //  Assert
        await().atMost(5, TimeUnit.SECONDS).until { done }
        assertEquals("All indexes", listOf(0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6), listOfIndexesVisited)
    }

    @Test
    fun shouldNotSkipSentence() {
        simulatedTextToSpeechUtteranceDuration = 100L
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                ":: The sound of the QSD disengaging was enough of a shock to the pink haired engineer, that it took her a long moment before she could react.  The work around they'd pulled together was more likely to leave the QSD as scrap rather than to safely shut down.  And it should have been running for a lot longer to arrive.  With a confused look on her face, she began the process of determining what had gone wrong, only to see that the bridge had taken the ship out of slipstream intentionally.  However, before she could ask them why, they called her! ::"
        )


        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        var done = false
        numOfUtterancesToProgressObservable.second.subscribe({
            println("Visited $it")
            listOfIndexesVisited.add(it)
        }, {}, {done=true})

        //  Assert
        await().atMost(5, TimeUnit.SECONDS).until { done }
        assertEquals("All indexes", listOf(0, 1, 2, 3, 4, 5, 6), listOfIndexesVisited)
    }

    @Test
    fun shouldPauseDictation() {
        //  Arrange
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived."
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        var wasPausedAlready = false
        var done = false
        numOfUtterancesToProgressObservable.second.subscribe({
            if (it == 3 && !wasPausedAlready) {
                ttsInteractor.pause()
                wasPausedAlready = true
            }
            listOfIndexesVisited.add(it)
        }, {}, {done=true})

        await().atMost(5, TimeUnit.SECONDS).until { wasPausedAlready }

        ttsInteractor.resume()

        await().atMost(5, TimeUnit.SECONDS).until { done }
        assertEquals("Visited items", listOf(0, 1, 2, 3, 3, 4), listOfIndexesVisited)
    }

    @Test
    fun shouldSeekToSpecificSentence() {
        //  Arrange
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived."
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        var seekAlreadyDone = false
        var done = false
        numOfUtterancesToProgressObservable.second.subscribe( {
            if (it == 2 && !seekAlreadyDone) {
                ttsInteractor.seekTo(1)
                seekAlreadyDone = true
            }
            listOfIndexesVisited.add(it)
        }, {}, {done=true})

        //  Assert
        await().atMost(10, TimeUnit.SECONDS).until { done }
        assertEquals("Visited items", listOf(0, 1, 2, 1, 2, 3, 4), listOfIndexesVisited)
    }

    @Test
    fun shouldIndicateThatIsPausedWhenDictationPaused() {
//  Arrange
        val sim = Sim(
                0L,
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

        // Assert
        await().atMost(5, TimeUnit.SECONDS).until { wasPausedAlready }

        assertTrue(ttsInteractor.isPaused())

    }

    @Test
    fun shouldProvideClose(){
        //  Arrange
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived."
        )

        //  Act
        val numOfUtterancesToProgressObservable = ttsInteractor.speakSims(listOf(sim))

        val listOfIndexesVisited = mutableListOf<Int>()
        var wasPausedAlready = false
        numOfUtterancesToProgressObservable.second.subscribe( {
            if (it == 3 && !wasPausedAlready) {
                ttsInteractor.pause()
                wasPausedAlready = true
            }
            listOfIndexesVisited.add(it)
        }, {}, {})

        // Assert
        await().atMost(5, TimeUnit.SECONDS).until { wasPausedAlready }

        ttsInteractor.close()
    }

}