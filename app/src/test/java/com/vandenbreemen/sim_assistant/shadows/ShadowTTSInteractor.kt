package com.vandenbreemen.sim_assistant.shadows

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractorImpl
import io.reactivex.Observable
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

var spokenSim : Sim? = null

/**
 *
 * @author kevin
 */
@Implements(TTSInteractorImpl::class)
class ShadowTTSInteractor{

    @Implementation
    fun speakSim(sim: Sim): Pair<Int, Observable<Int>> {
        spokenSim = sim
        return Pair(1, Observable.just(1))
    }

}