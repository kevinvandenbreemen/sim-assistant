package com.vandenbreemen.sim_assistant.ui

import android.widget.Spinner
import com.vandenbreemen.sim_assistant.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import java.util.function.Consumer

@RunWith(RobolectricTestRunner::class)
class SelectSimByTitleTest {

    lateinit var selectSimByTitle: SelectSimByTitle

    var selectedIndex: Int? = null

    @Before
    fun setup() {

        val app = RuntimeEnvironment.application

        this.selectSimByTitle = SelectSimByTitle(
                app,
                null
        )
    }

    @Test
    fun shouldDisplaySims() {
        //  Arrange
        val simTitlesToDictationIndexes: List<Pair<String, Int>> =
                listOf(Pair("Sim 1", 0), Pair("Sim 2", 33))

        //  Act
        this.selectSimByTitle.setSimSelections(simTitlesToDictationIndexes)

        //  Assert
        val spinner = selectSimByTitle.findViewById<Spinner>(R.id.simSelector)
        assertEquals("Two items", 2, spinner.adapter.count)
        assertEquals("First Sim", "Sim 1", spinner.getItemAtPosition(0))
        assertEquals("Second Sim", "Sim 2", spinner.getItemAtPosition(1))
    }

    @Test
    fun shouldSelectSim() {
        //  Arrange

        selectSimByTitle.simSelectionListener = Consumer { index -> selectedIndex = index }

        val simTitlesToDictationIndexes: List<Pair<String, Int>> =
                listOf(Pair("Sim 1", 0), Pair("Sim 2", 33))
        this.selectSimByTitle.setSimSelections(simTitlesToDictationIndexes)

        //  Act
        val spinner = selectSimByTitle.findViewById<Spinner>(R.id.simSelector)
        val shadowSpinner = shadowOf(spinner)
        shadowSpinner.selectItemWithText("Sim 2")

        //  Assert
        assertNotNull("Index select", selectedIndex)
        assertEquals("Index Select", 33, selectedIndex)

    }

    @Test
    fun shouldUpdateSelectedSimByTitle(){

        //  Arrange
        selectSimByTitle.simSelectionListener = Consumer { index -> selectedIndex = index }
        val simTitlesToDictationIndexes: List<Pair<String, Int>> =
                listOf(Pair("Sim 1", 0), Pair("Sim 2", 33))
        this.selectSimByTitle.setSimSelections(simTitlesToDictationIndexes)
        val spinner = selectSimByTitle.findViewById<Spinner>(R.id.simSelector)
        //val shadowSpinner = shadowOf(spinner)

        //  Act
        selectSimByTitle.setSelectedSim("Sim 2")

        //  Assert
        assertEquals("Selected Item", "Sim 2", spinner.selectedItem)
        assertEquals("No Update Expected", null, selectedIndex)
    }

    @Test
    fun shouldUpdateSelectionAfterProgrammaticUpdate(){
        //  Arrange
        selectSimByTitle.simSelectionListener = Consumer { index -> selectedIndex = index }
        val simTitlesToDictationIndexes: List<Pair<String, Int>> =
                listOf(Pair("Sim 1", 0), Pair("Sim 2", 33))
        this.selectSimByTitle.setSimSelections(simTitlesToDictationIndexes)
        val spinner = selectSimByTitle.findViewById<Spinner>(R.id.simSelector)
        //val shadowSpinner = shadowOf(spinner)

        //  Act
        selectSimByTitle.setSelectedSim("Sim 2")
        val shadowSpinner = shadowOf(spinner)
        shadowSpinner.selectItemWithText("Sim 1")

        //  Assert
        assertEquals("Selected Item", "Sim 1", spinner.selectedItem)
        assertEquals("Update expected", 0, selectedIndex)
    }

}