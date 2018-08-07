package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListModel
import io.reactivex.Observable
import java.util.*

class KnownSimListModel(val list: List<Sim>) : SimListModel {

    var selectedSims = mutableListOf<Sim>()

    override fun getSimList(): Observable<Sim> {
        return Observable.fromArray(*list.toTypedArray())
    }

    override fun selectedSims(): List<Sim> {
        return Collections.unmodifiableList(selectedSims)
    }

    override fun selectSim(sim: Sim) {
        selectedSims.add(sim)
    }

    override fun hasSelectedSims(): Boolean {
        return selectedSims.isNotEmpty()
    }

    override fun simSelected(sim: Sim): Boolean {
        return selectedSims.contains(sim)
    }

    override fun deselectSim(sim: Sim) {
        this.selectedSims.remove(sim)
    }
}