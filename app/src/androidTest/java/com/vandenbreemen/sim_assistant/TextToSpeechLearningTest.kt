package com.vandenbreemen.sim_assistant

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.ERROR
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TextToSpeechLearningTest {

    lateinit var tts: TextToSpeech

    @Test
    fun shouldSpeak() {

        var initialized = false

        val appContext = InstrumentationRegistry.getTargetContext()
        tts = TextToSpeech(appContext, TextToSpeech.OnInitListener { status ->
            initialized = true
            if (status != ERROR) {
                tts.setLanguage(Locale.US)
            }
        })

        await().atMost(5, TimeUnit.SECONDS).until { initialized }
        tts.speak("This is a test", QUEUE_FLUSH, null, null)

        Thread.sleep(1000)
    }

}