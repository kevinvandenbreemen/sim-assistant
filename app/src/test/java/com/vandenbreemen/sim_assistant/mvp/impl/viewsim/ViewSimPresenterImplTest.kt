package com.vandenbreemen.sim_assistant.mvp.impl.viewsim

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
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
                Pair(5, Observable.just(1, 2, 3, 4, 5))
        )
    }

    @Test
    fun shouldDictateSim(){
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(ttsInteractor).speakSims(listOf(sim1))
    }

    @Test
    fun shouldEnablePauseButtonOnceDictationBegins() {
        //  Act
        viewSimPresenter.speakSims()

        //  Assert
        verify(viewSimView).setPauseDictationEnabled(true)
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
        viewSimPresenter.speakSims()

        //  Act
        viewSimPresenter.pause()

        //  Assert
        verify(viewSimView).setPauseDictationEnabled(false)
    }

}