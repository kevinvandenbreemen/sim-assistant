package com.vandenbreemen.sim_assistant.mvp.tts

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.SUCCESS
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowTextToSpeech

@Implements(TextToSpeech::class)
class ShadowTTSExt : ShadowTextToSpeech() {

    companion object {
        val UTTERANCE_TIME = 1000L
    }

    var lastStartTime = 0L

    @Implementation
    override fun speak(text: CharSequence,
                       queueMode: Int,
                       params: Bundle?,
                       utteranceId: String?): Int {
        println("Speaking '$text'")
        super.speak(text, queueMode, params, utteranceId)
        lastStartTime = System.currentTimeMillis()
        return SUCCESS
    }

    @Implementation
    override fun stop(): Int {
        println("STOPPING")
        return super.stop()
    }

    @Implementation
    fun isSpeaking(): Boolean {
        return !super.isStopped() && System.currentTimeMillis() - lastStartTime < UTTERANCE_TIME
    }
}