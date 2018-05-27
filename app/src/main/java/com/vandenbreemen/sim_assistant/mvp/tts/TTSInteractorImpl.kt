package com.vandenbreemen.sim_assistant.mvp.tts

import android.content.Context
import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Observable

class TTSInteractorImpl(context: Context) : TTSInteractor {
    override fun speakSim(sim: Sim): Pair<Int, Observable<Int>> {
        val utterances = mutableListOf<String>()
        utterances.add(sim.title)
        utterances.add("By ${sim.author}")

        utterances.addAll("[.]+".toRegex().split(sim.content))

        return Pair<Int, Observable<Int>>(utterances.size, Observable.just(1))
    }

}
