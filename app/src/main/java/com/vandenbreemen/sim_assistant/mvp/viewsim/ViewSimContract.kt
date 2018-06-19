package com.vandenbreemen.sim_assistant.mvp.viewsim

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.headphones.HeadphonesReactionInteractor

interface ViewSimPresenter{
    fun start()
    fun speakSims()
    fun pause()
    fun seekTo(index: Int)
    fun close()
    fun getHeadphonsReactionInteractor(): HeadphonesReactionInteractor
    fun setRepeat()
}

interface ViewSimModel{
    fun getSims(): List<Sim>

}

interface ViewSimView{
    fun displaySim(sim:Sim)
    fun setDictationControlsEnabled(enabled: Boolean)
    fun setSpeakSimsEnabled(enabled: Boolean)
    fun updateProgress(index: Int)
    fun setTotalUtterancesToBeSpoken(totalUtterances: Int)
    fun setDictationProgressVisible(visible: Boolean)
    fun setDictationProgressEnabled(enabled: Boolean)
    fun setSelections(simTitlesToDictationIndexes: List<Pair<String, Int>>)
    fun updateSelectedSim(currentSimTitle: String)
    fun setSimSelectorEnabled(enabled: Boolean)
    fun setHeadphonesInteractor(headphonesReactionInteractor: HeadphonesReactionInteractor)
    fun toggleRepeatDictationOn()
    fun toggleRepeatDictationOff()
}