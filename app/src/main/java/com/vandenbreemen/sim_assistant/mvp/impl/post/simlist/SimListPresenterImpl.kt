package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListModel
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class SimListPresenterImpl(private val simListModel: SimListModel):SimListPresenter {

    lateinit var view:SimListView

    override fun start(view: SimListView) {
        this.view = view

        simListModel.getSimList().observeOn(mainThread()).subscribe({
            sim->view.addSimItem(sim)
        },{

        },{

        })
    }

    override fun viewSelectedSims() {
        view.viewSelectedSims(simListModel.selectedSims())
    }

    override fun viewSim(sim: Sim) {
        view.viewSim(sim)
    }

    override fun selectSim(sim: Sim) {
        simListModel.selectSim(sim)
        if (simListModel.hasSelectedSims()) {
            view.displayViewSelectedSimsOption()
            view.selectSim(sim)
        }
    }
}