package com.vandenbreemen.sim_assistant.mvp.tags

import com.vandenbreemen.sim_assistant.api.sim.Sim

interface SimTagsPresenter {

    fun searchTags(criteria: String)

    fun start(sim: Sim)

}

interface SimTagsView {

}