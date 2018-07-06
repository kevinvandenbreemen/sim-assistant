package com.vandenbreemen.sim_assistant.mvp.simitem

import com.vandenbreemen.sim_assistant.api.sim.Sim

interface SimItemPresenter {

    fun openTags(sim: Sim)

}

interface SimItemView {

    fun showSimTagsDialog(sim: Sim)

}