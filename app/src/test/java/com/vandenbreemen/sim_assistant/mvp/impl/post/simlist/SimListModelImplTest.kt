package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

class SimListModelImplTest {
    @get:Rule
    val mockingRule = MockitoJUnit.rule()

    @Mock
    lateinit var postRepository: PostRepository


    lateinit var sim: Sim

    @Before
    fun setup() {
        this.sim = Sim(
                0,
                "Test", "Kevin", 0, "This is a test"
        )

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
    fun selectedSimsShouldBeSortedInAscendingOrderOfDate(){
        //  Arrange

        sim = Sim(
                1,
                "Test", "Kevin", 444333, "This is a test"
        )

        var sim1 = Sim(
                2,
                "Test", "Kevin", 1231, "This is a test"
        )

        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.selectSim(sim)
        simListModel.selectSim(sim1)

        //  Assert
        val selectedSims = simListModel.selectedSims()
        assertEquals(sim1, selectedSims[0])
        assertEquals(sim, selectedSims[1])

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
    fun shouldMarkSimSelected() {
        //  Arrange
        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.selectSim(sim)

        //  Assert
        assertTrue("sim selected", sim.selected)
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

    @Test
    fun shouldMarkSimAsNonSelectedWhenDeselecting() {
        //  Arrange
        sim.selected = true
        val simListModel = SimListModelImpl(postRepository)

        //  Act
        simListModel.deselectSim(sim)

        //  Assert
        assertFalse("Sim deselected", sim.selected)

    }
}