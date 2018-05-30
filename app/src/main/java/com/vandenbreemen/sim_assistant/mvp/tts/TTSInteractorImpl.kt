package com.vandenbreemen.sim_assistant.mvp.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.ERROR
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.SimParser
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import java.lang.Thread.sleep
import java.util.*

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

    override fun speakSim(sim: Sim): Pair<Int, Observable<Int>> {
        val utterances = SimParser(sim).toUtterances()

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
