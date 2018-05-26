package com.vandenbreemen.sim_assistant.mvp.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Observable

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
interface SimListPresenter {

    fun start(view:SimListView)
    fun viewSim(sim:Sim)
    fun selectSim(sim: Sim)
    fun viewSelectedSims()

}

interface SimListView {

    fun addSimItem(sim:Sim)
    fun viewSim(sim: Sim)
    fun displayViewSelectedSimsOption()
    fun viewSelectedSims(sims: List<Sim>)
    fun selectSim(sim: Sim)
    fun hideViewSelectedSimsOption()
    fun deselectSim(sim: Sim)
}

interface SimListModel{
    fun getSimList(): Observable<Sim>
    fun selectedSims(): List<Sim>
    fun selectSim(sim: Sim)
    fun hasSelectedSims(): Boolean
    fun simSelected(sim: Sim): Boolean
    fun deselectSim(sim: Sim)
}
