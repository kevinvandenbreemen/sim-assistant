package com.vandenbreemen.sim_assistant.mvp.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.SUCCESS
import android.speech.tts.UtteranceProgressListener
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowTextToSpeech

var simulatedTextToSpeechUtteranceDuration: Long? = null

@Implements(TextToSpeech::class)
class ShadowTTSExt : ShadowTextToSpeech() {

    companion object {
        val DEFAULT_SIMULATED_TTS_UTTERANCE_DURATION = 500L
    }

    var lastStartTime = 0L

    var utteranceProgressListener: UtteranceProgressListener? = null

    @Implementation
    override fun __constructor__(context: Context, listener: TextToSpeech.OnInitListener) {
        super.__constructor__(context, listener)

        object : Thread() {
            override fun run() {
                if (Thread.currentThread().equals(this)) {
                    listener.onInit(SUCCESS)
                }
            }
        }.start()

    }

    @Implementation
    override fun speak(text: CharSequence,
                       queueMode: Int,
                       params: Bundle?,
                       utteranceId: String?): Int {
        println("Speaking '$text'")
        super.speak(text, queueMode, params, utteranceId)
        lastStartTime = System.currentTimeMillis()
        utteranceProgressListener?.let { listener ->
            object : Thread() {
                override fun run() {
                    if (Thread.currentThread().equals(this)) {
                        Thread.sleep(simulatedTextToSpeechUtteranceDuration
                                ?: DEFAULT_SIMULATED_TTS_UTTERANCE_DURATION)
                        listener.onDone(utteranceId)
                    }
                }
            }.start()

        }
        return SUCCESS
    }

    @Implementation
    fun setOnUtteranceProgressListener(listener: UtteranceProgressListener): Int {
        println("Got progress listener")
        this.utteranceProgressListener = listener
        return SUCCESS
    }

    @Implementation
    override fun stop(): Int {
        println("STOPPING")
        return super.stop()
    }

    @Implementation
    fun isSpeaking(): Boolean {
        return !super.isStopped() && System.currentTimeMillis() - lastStartTime < simulatedTextToSpeechUtteranceDuration ?: DEFAULT_SIMULATED_TTS_UTTERANCE_DURATION
    }
}