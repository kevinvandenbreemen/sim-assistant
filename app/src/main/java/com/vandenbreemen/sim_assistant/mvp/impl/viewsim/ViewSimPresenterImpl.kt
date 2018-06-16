package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.mvp.headphones.HeadphonesReactionInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimModel
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread

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
            val simDictationDetailsToUtteranceDictation = ttsInteractor.speakSims(viewSimModel.getSims())

            viewSimView.setTotalUtterancesToBeSpoken(simDictationDetailsToUtteranceDictation.first.numberOfSentences)

            val sortedPositions = simDictationDetailsToUtteranceDictation.first.simPositions.map {
                Pair<String, Int>(it.key.title, it.value)
            }.sortedBy { pair -> pair.second }

            val positionToSimTitle = mapOf<Int, String>(
                    * simDictationDetailsToUtteranceDictation.first.simPositions.map {
                        Pair(it.value, it.key.title)
                    }.toTypedArray()
            )

            viewSimView.setSelections(sortedPositions)

            simDictationDetailsToUtteranceDictation
                    .second.observeOn(mainThread())
                    .subscribe(
                            {
                                viewSimView.updateProgress(it)

                                var idx = it
                                var foundNearestUtteranceIndexToCorrespondingSim = false
                                while (idx > -1 && !foundNearestUtteranceIndexToCorrespondingSim) {
                                    positionToSimTitle[idx]?.let { simTitle ->
                                        viewSimView.updateSelectedSim(simTitle)
                                        foundNearestUtteranceIndexToCorrespondingSim = true
                                    }
                                    idx --
                                }

                            },
                            {},
                            {
                                viewSimView.setSpeakSimsEnabled(true)
                                viewSimView.setPauseDictationEnabled(false)
                                viewSimView.setDictationProgressVisible(false)
                            })
        }

        viewSimView.setPauseDictationEnabled(true)
        viewSimView.setSpeakSimsEnabled(false)
        viewSimView.setDictationProgressVisible(true)
        viewSimView.setDictationProgressEnabled(true)
        viewSimView.setSimSelectorEnabled(true)
    }

    override fun getHeadphonsReactionInteractor(): HeadphonesReactionInteractor {
        return TextToSpeechHeadphonesInteractor(this, ttsInteractor)
    }

    override fun close() {
        ttsInteractor.close()
    }

    override fun seekTo(index: Int) {
        ttsInteractor.seekTo(index)
    }

    override fun pause() {
        ttsInteractor.pause()
        viewSimView.setPauseDictationEnabled(false)
        viewSimView.setSpeakSimsEnabled(true)
        viewSimView.setDictationProgressEnabled(false)
        viewSimView.setSimSelectorEnabled(false)
    }

    override fun start() {
        viewSimView.setHeadphonesInteractor(getHeadphonsReactionInteractor())
        viewSimModel.getSims().forEach { sim->
            viewSimView.displaySim(sim)
        }
    }



}