package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.mvp.headphones.HeadphonesReactionInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter

class TextToSpeechHeadphonesInteractor(private val viewSimPresenter: ViewSimPresenter, private val ttsInteractor: TTSInteractor) : HeadphonesReactionInteractor {
    override fun onHeadphonesDisconnected() {
        if (ttsInteractor.isInProcessOfSpeakingSims() && !ttsInteractor.isPaused()) {
            viewSimPresenter.pause()
        }
    }

    override fun onHeadphonesConnected() {

    }
}
