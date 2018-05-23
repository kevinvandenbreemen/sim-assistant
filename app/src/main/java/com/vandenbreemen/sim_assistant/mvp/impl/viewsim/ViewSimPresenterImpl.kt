package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimModel
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class ViewSimPresenterImpl(private val viewSimModel: ViewSimModel, private val viewSimView: ViewSimView):ViewSimPresenter {

    override fun start() {
        viewSimModel.getSims().forEach { sim->
            viewSimView.displaySim(sim)
        }
    }



}