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

//  How to write to file
//  https://stackoverflow.com/a/4976327/2328196

class TTSInteractorImpl(context: Context) : TTSInteractor {

    companion object {
        const val TAG = "TTSInteractor"
    }

    lateinit var tts:TextToSpeech

    init {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status->
            if(status != ERROR){
                tts.language = Locale.US
            }
        })
    }

    private fun waitForTTSCompletion(){
        while(tts.isSpeaking){
            sleep(200)
        }
    }

    override fun speakSims(sims: List<Sim>): Pair<Int, Observable<Int>> {

        val utterances = mutableListOf<String>()
        sims.forEach {
            utterances.addAll(SimParser(it).toUtterances())
        }

        val observable = Observable.create(ObservableOnSubscribe<Int> { emitter ->
            for (i in 0 until utterances.size) {
                emitter.onNext(i)
                waitForTTSCompletion()
                tts.speak(utterances[i], QUEUE_FLUSH, null, null)
            }

            emitter.onComplete()
        }).subscribeOn(computation())

        return Pair<Int, Observable<Int>>(utterances.size, observable)
    }

}
