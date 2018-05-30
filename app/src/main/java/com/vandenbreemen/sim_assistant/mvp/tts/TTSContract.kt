package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Observable

interface TTSInteractor {
    fun speakSims(vararg sim: Sim): Pair<Int, Observable<Int>>

}