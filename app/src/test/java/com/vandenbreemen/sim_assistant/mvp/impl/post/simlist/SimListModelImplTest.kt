package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import io.reactivex.Observable
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit

class SimListModelImplTest {
    @get:Rule
    val mockingRule = MockitoJUnit.rule()

    @Mock
    lateinit var postRepository: PostRepository

    @Mock
    lateinit var sim: Sim

    @Before
    fun setup() {
        `when`(postRepository.getPosts()).thenReturn(Observable.just(sim))
    }

    @Test
    fun shouldSelectSim() {

        //  Arrange
        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.selectSim(sim)

        //  Assert
        assertEquals("Selected sims", 1, simListModel.selectedSims().size)

    }

    @Test
    fun shouldDetermineIfSimsSelected() {
        //  Arrange
        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.selectSim(sim)

        //  Assert
        assertTrue("Has selected", simListModel.hasSelectedSims())
    }

    @Test
    fun shouldKnowSimIsSelected() {
        //  Arrange
        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.selectSim(sim)

        //  Assert
        assertTrue("sim selected", simListModel.simSelected(sim))
    }

    @Test
    fun shouldDeSelectSim() {
        //  Arrange
        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.selectSim(sim)
        simListModel.deselectSim(sim)

        //  Assert
        assertFalse("sim selected", simListModel.simSelected(sim))
    }
}