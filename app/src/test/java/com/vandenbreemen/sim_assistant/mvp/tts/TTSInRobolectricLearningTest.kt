package com.vandenbreemen.sim_assistant.mvp.tts

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class TTSInRobolectricLearningTest {

    @Test
    fun howToCreateTTSAndSpeakWithItInRobolectric() {

        lateinit var tts: TextToSpeech

        tts = TextToSpeech(RuntimeEnvironment.application.applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.US
            }
        })

        tts.speak("This is a test", QUEUE_FLUSH, null, null)

        while (tts.isSpeaking) {
            Thread.sleep(200)
        }


    }

}