package com.vandenbreemen.sim_assistant.mvp.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.ERROR
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.SimParser
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers.computation
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

//  How to write to file
//  https://stackoverflow.com/a/4976327/2328196

class TTSInteractorImpl(context: Context) : TTSInteractor {

    companion object {
        const val TAG = "TTSInteractor"
    }

    lateinit var tts:TextToSpeech

    var stringsToSpeak: List<String>? = null

    val indexOfCurrentStringBeingSpoken = AtomicInteger(-1)

    val paused = AtomicBoolean(false)

    init {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status->
            if(status != ERROR){
                tts.language = Locale.US
            }
        })
    }

    private fun hasMoreStrings() = indexOfCurrentStringBeingSpoken.get() < (stringsToSpeak?.size
            ?: -1) - 1

    private fun waitForTTSCompletion(){
        while (tts.isSpeaking || paused.get()) {
            sleep(200)
        }
    }

    override fun speakSims(sims: List<Sim>): Pair<Int, Observable<Int>> {

        val utterances = mutableListOf<String>()
        sims.forEach {
            utterances.addAll(SimParser(it).toUtterances())
        }
        stringsToSpeak = listOf(*utterances.toTypedArray())

        val observable = Observable.create(ObservableOnSubscribe<Int> { emitter ->

            while (hasMoreStrings()) {
                waitForTTSCompletion()
                val nextIndex = indexOfCurrentStringBeingSpoken.incrementAndGet()
                emitter.onNext(nextIndex)
                tts.speak(utterances[nextIndex], QUEUE_FLUSH, null, null)
            }

            emitter.onComplete()
        }).subscribeOn(computation())

        return Pair<Int, Observable<Int>>(stringsToSpeak!!.size, observable)
    }

    override fun pause() {
        stringsToSpeak?.let {
            paused.set(true)
            tts.stop()
            indexOfCurrentStringBeingSpoken.decrementAndGet()
            println("Decrement to ${indexOfCurrentStringBeingSpoken.get()}")
        }
    }

    override fun resume() {
        paused.set(false)
    }

}
