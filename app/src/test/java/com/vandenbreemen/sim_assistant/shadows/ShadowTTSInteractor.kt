package com.vandenbreemen.sim_assistant.shadows

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.tts.SimDictationDetails
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractorImpl
import io.reactivex.Observable
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

var spokenSim : MutableList<Sim> = mutableListOf()

/**
 *
 * @author kevin
 */
@Implements(TTSInteractorImpl::class)
class ShadowTTSInteractor{

    @Implementation
    fun speakSims(sims: List<Sim>): Pair<SimDictationDetails, Observable<Int>> {
        spokenSim.addAll(sims)
        return Pair(SimDictationDetails(1), Observable.just(1))
    }

}