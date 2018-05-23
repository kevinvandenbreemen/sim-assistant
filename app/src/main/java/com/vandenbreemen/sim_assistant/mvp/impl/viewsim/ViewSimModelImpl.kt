package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimModel

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class ViewSimModelImpl(private val sims:Array<Sim>):ViewSimModel {

    override fun getSims():List<Sim>{
        return sims.asList()
    }

}