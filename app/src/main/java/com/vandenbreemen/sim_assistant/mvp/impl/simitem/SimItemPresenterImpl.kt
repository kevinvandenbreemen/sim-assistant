package com.vandenbreemen.sim_assistant.mvp.impl.simitem

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.simitem.SimItemPresenter
import com.vandenbreemen.sim_assistant.mvp.simitem.SimItemView

class SimItemPresenterImpl(val view: SimItemView) : SimItemPresenter {
    override fun openTags(sim: Sim) {
        view.showSimTagsDialog()
    }
}