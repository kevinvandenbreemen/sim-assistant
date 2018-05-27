package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView
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
 * <h2>Intro</h2>
 *
 *
 * <h2>Other Details</h2>
 *
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class SimListPresenterImplTest{

    @get:Rule
    val mockingRule = MockitoJUnit.rule()

    @Mock
    lateinit var postRepository: PostRepository

    @Mock
    lateinit var simListView: SimListView

    @Mock
    lateinit var sim:Sim

    lateinit var presenter: SimListPresenter

    @Before
    fun setup(){
        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim))
        presenter = SimListPresenterImpl(SimListModelImpl(postRepository))
        presenter.start(simListView)
    }

    @Test
    fun shouldViewSim(){

        //  Act
        presenter.viewSim(sim)

        //  Assert
        verify(simListView).viewSim(sim)

    }

    @Test
    fun shouldAlertViewOnSelectSim() {
        //  Act
        presenter.selectSim(sim)

        //  Assert
        verify(simListView).displayViewSelectedSimsOption()
        verify(simListView).selectSim(sim)
    }

    @Test
    fun shouldDeselectSim() {
        //  Act
        presenter.selectSim(sim)
        presenter.selectSim(sim)

        //  Assert
        verify(simListView).displayViewSelectedSimsOption()
        verify(simListView).selectSim(sim)
        verify(simListView).hideViewSelectedSimsOption()
        verify(simListView).deselectSim(sim)
    }

    @Test
    fun shouldViewSelectedSims(){
        //  Act
        presenter.selectSim(sim)
        presenter.viewSelectedSims()

        //  Assert
        verify(simListView).viewSelectedSims(listOf(sim))
    }


}