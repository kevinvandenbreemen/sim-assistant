package com.vandenbreemen.sim_assistant.mvp.viewsim

import com.vandenbreemen.sim_assistant.api.sim.Sim

interface ViewSimPresenter{
    fun start()
    fun speakSims()
    fun pause()
    fun seekTo(index: Int)
    fun close()
}

interface ViewSimModel{
    fun getSims(): List<Sim>

}

interface ViewSimView{
    fun displaySim(sim:Sim)
    fun setPauseDictationEnabled(enabled: Boolean)
    fun setSpeakSimsEnabled(enabled: Boolean)
    fun updateProgress(index: Int)
    fun setTotalUtterancesToBeSpoken(totalUtterances: Int)
    fun setDictationProgressVisible(visible: Boolean)
    fun setDictationProgressEnabled(enabled: Boolean)
    fun setSelections(simTitlesToDictationIndexes: List<Pair<String, Int>>)
    fun updateSelectedSim(currentSimTitle: String)
    fun setSimSelectorEnabled(enabled: Boolean)
}