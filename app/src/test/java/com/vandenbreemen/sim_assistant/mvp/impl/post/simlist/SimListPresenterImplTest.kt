package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView
import io.reactivex.Observable
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
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
        `when`(postRepository.getPosts(100)).thenReturn(Observable.just(sim))
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
    fun shouldTellViewToHideProgressBarWhenDone() {

        //  Act
        presenter.start(simListView)

        //  Assert
        verify(simListView, times(2) /*This will happen a second time due to setup() */).hideProgressSpinner()
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
    fun shouldMarkSimSelectedWhenSelected() {

        //  arrange
        val testSim = Sim(
                0,
                "Test", "Kevin", 0, "This is a test"
        )

        //  Act
        presenter.selectSim(testSim)

        //  Assert
        assertTrue("Sim Selected", testSim.selected)
    }

    @Test
    fun shouldMarkSimAsNotSelectedWhenDeselected() {
        //  arrange
        val testSim = Sim(
                0,
                "Test", "Kevin", 0, "This is a test"
        )

        //  Act
        presenter.selectSim(testSim)
        presenter.selectSim(testSim)

        //  Assert
        assertFalse("Sim selected", testSim.selected)
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