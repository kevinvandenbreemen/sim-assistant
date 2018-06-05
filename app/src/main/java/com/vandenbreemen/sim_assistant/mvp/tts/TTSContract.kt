package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Observable

interface TTSInteractor {
    fun speakSims(sims: List<Sim>): Pair<Int, Observable<Int>>
    fun pause()
    fun resume()
    fun isPaused(): Boolean
    fun seekTo(position: Int)
    fun close()

}