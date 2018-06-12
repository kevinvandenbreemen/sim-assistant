package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.tts.SimDictationDetails
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

/**
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class ViewSimPresenterImplTest{

    @get:Rule
    val rule = MockitoJUnit.rule()

    lateinit var viewSimPresenter:ViewSimPresenter

    @Mock
    lateinit var viewSimView:ViewSimView

    @Mock
    lateinit var ttsInteractor:TTSInteractor

    val sim1 = Sim(
            "test sim", "Kevin", System.currentTimeMillis(),
            "This is a test"
    )

    @Before
    fun setup(){

        viewSimPresenter = ViewSimPresenterImpl(ViewSimModelImpl(arrayOf(sim1)),
                viewSimView, ttsInteractor
                )

        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(
                Pair(SimDictationDetails(5, mapOf(Pair<Sim,Int>(sim1, 0))), Observable.just(0, 1, 2, 3, 4))
        )

        `when`(ttsInteractor.isPaused()).thenReturn(false)
    }

    @Test
    fun shouldDictateSim(){
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(ttsInteractor).speakSims(listOf(sim1))
    }

    @Test
    fun shouldTellViewToCreateSimSelectorWhenDictatingSims() {

        //  Arrange
        val sim2 = Sim(
                "Another Sim", "Kevin", 0, "This is a test"
        )
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(
                Pair(SimDictationDetails(5,
                        mapOf(Pair<Sim, Int>(sim1, 0), Pair<Sim, Int>(sim2, 3))), Observable.just(0, 1, 2, 3, 4))
        )

        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setSelections(listOf(Pair<String, Int>(sim1.title, 0), Pair<String, Int>(sim2.title, 3)))
    }

    @Test
    fun shouldUpdateSimSelectionDuringDictation() {

        //  Arrange
        val expectedUtterancesInEachSim = 3
        val sim2 = Sim(
                "Another Sim", "Kevin", 0, "This is a test"
        )
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(
                Pair(SimDictationDetails(5,
                        mapOf(Pair<Sim, Int>(sim1, 0), Pair<Sim, Int>(sim2, 3))), Observable.just(0, 1, 2, 3, 4))
        )

        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView, times(expectedUtterancesInEachSim)).updateSelectedSim(sim1.title)
        verify(viewSimView, times(expectedUtterancesInEachSim-1)).updateSelectedSim(sim2.title)

    }

    @Test
    fun shouldReEnableSpeakSimsWhenSpeakingFinished() {
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setSpeakSimsEnabled(true)
        verify(viewSimView).setPauseDictationEnabled(false)
    }

    @Test
    fun shouldEnablePauseButtonOnceDictationBegins() {
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setPauseDictationEnabled(true)
    }

    @Test
    fun shouldShowSimSelectorOnceDictationBegins(){
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setSimSelectorEnabled(true)
    }

    @Test
    fun shouldPauseSimsWhenPauseCalled() {
        //  Arrange
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        //  Assert
        verify(ttsInteractor).pause()

    }

    @Test
    fun shouldDisablePauseButtonWhenPaused() {
        //  Arrange
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(Pair(SimDictationDetails(1, mapOf(Pair<Sim,Int>(sim1, 0))), Observable.create(ObservableOnSubscribe<Int> { })))
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        //  Assert
        verify(viewSimView).setPauseDictationEnabled(false)
    }

    @Test
    fun shouldDisableSimSelectorWhenPaused(){
        //  Arrange
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(Pair(SimDictationDetails(1, mapOf(Pair<Sim,Int>(sim1, 0))), Observable.create(ObservableOnSubscribe<Int> { })))
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        //  Assert
        verify(viewSimView).setSimSelectorEnabled(false)
    }

    @Test
    fun shouldDisableProgressBarWhenPaused(){
        //  Arrange
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(Pair(SimDictationDetails(1, mapOf(Pair<Sim,Int>(sim1, 0))), Observable.create(ObservableOnSubscribe<Int> { })))
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        //  Assert
        verify(viewSimView).setDictationProgressEnabled(false)
    }

    @Test
    fun shouldDisableSpeakSimsWhenSpeaking() {
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setSpeakSimsEnabled(false)
    }

    @Test
    fun shouldReEnableSpeakSimsWhenPausing() {
        //  Arrange
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(Pair(SimDictationDetails(1, mapOf(Pair<Sim,Int>(sim1, 0))), Observable.create(ObservableOnSubscribe<Int> { })))
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        //  Assert
        verify(viewSimView).setSpeakSimsEnabled(true)
    }

    @Test
    fun shouldResumeDictationWhenSpeakSimsCalledAfterPause() {
        //  Arrange
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        `when`(ttsInteractor.isPaused()).thenReturn(true)

        viewSimPresenter.speakSims()

        //  Assert
        verify(ttsInteractor).resume()

    }

    @Test
    fun shouldReEnableSeekBarWhenSpeakSimsCalledAfterPause(){
        //  Arrange
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        `when`(ttsInteractor.isPaused()).thenReturn(true)

        reset(viewSimView)
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setDictationProgressEnabled(true)
    }

    @Test
    fun shouldSeek() {
        //  Arrange
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.seekTo(2)

        //  Assert
        verify(ttsInteractor).seekTo(2)
    }

    @Test
    fun shouldUpdateViewWithProgress() {
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).updateProgress(0)
        verify(viewSimView).updateProgress(1)
        verify(viewSimView).updateProgress(2)
        verify(viewSimView).updateProgress(3)
        verify(viewSimView).updateProgress(4)
    }

    @Test
    fun shouldTellViewTotalNumberOfUtterances() {
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setTotalUtterancesToBeSpoken(5)
    }

    @Test
    fun shouldTellViewToShowProgressBarWhenSpeaking(){

        //  Arrange
        `when`(ttsInteractor.speakSims(listOf(sim1))).thenReturn(Pair(SimDictationDetails(1, mapOf(Pair<Sim,Int>(sim1, 0))), Observable.create(ObservableOnSubscribe<Int> { })))

        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setDictationProgressVisible(true)

    }

    @Test
    fun shouldTellViewToHideProgressBarWhenDoneSpeaking(){
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setDictationProgressVisible(true)
        verify(viewSimView).setDictationProgressVisible(false)
    }

    @Test
    fun shouldProvideClose(){
        //  Act
        viewSimPresenter.close()

        //  Assert
        verify(ttsInteractor).close()
    }

}