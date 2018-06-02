package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimModel
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class ViewSimPresenterImpl(private val viewSimModel: ViewSimModel, private val viewSimView: ViewSimView, val ttsInteractor: TTSInteractor):ViewSimPresenter {
    override fun speakSims() {
        if (ttsInteractor.isPaused()) {
            ttsInteractor.resume()
        } else {
            ttsInteractor.speakSims(viewSimModel.getSims())
                    .second.subscribe()
        }

        viewSimView.setPauseDictationEnabled(true)
        viewSimView.setSpeakSimsEnabled(false)
    }

    override fun pause() {
        ttsInteractor.pause()
        viewSimView.setPauseDictationEnabled(false)
        viewSimView.setSpeakSimsEnabled(true)
    }

    override fun start() {
        viewSimModel.getSims().forEach { sim->
            viewSimView.displaySim(sim)
        }
    }



}