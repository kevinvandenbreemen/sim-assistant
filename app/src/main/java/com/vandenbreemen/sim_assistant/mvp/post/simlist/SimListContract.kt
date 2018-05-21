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

}

interface SimListView {

    fun addSimItem(sim:Sim)

}

interface SimListModel{
    fun getSimList(): Observable<Sim>
}
