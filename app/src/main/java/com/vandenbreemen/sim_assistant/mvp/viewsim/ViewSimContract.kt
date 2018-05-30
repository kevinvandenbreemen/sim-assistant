package com.vandenbreemen.sim_assistant.mvp.viewsim

import com.vandenbreemen.sim_assistant.api.sim.Sim

interface ViewSimPresenter{
    fun start()
    fun speakSims()
}

interface ViewSimModel{
    fun getSims(): List<Sim>

}

interface ViewSimView{
    fun displaySim(sim:Sim)
}