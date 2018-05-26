package com.vandenbreemen.sim_assistant.mvp.tts

import android.content.Context
import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Observable

class TTSInteractorImpl(context: Context) : TTSInteractor {
    override fun speakSim(sim: Sim): Pair<Int, Observable<Int>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
