package com.vandenbreemen.sim_assistant.api.sim

import org.junit.Assert.assertEquals
import org.junit.Test

class SimParserTest {

    @Test
    fun shouldRecognizeSettingAsSeparateUtterance() {

        //  Arrange
        val sim = Sim("Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor))\n\n::It was a dark and stormy night.::")

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        assertEquals("Utterance Count", 4, utterances.size)
        assertEquals("First Part of Content", "Corridor", utterances[2])
    }

    @Test
    fun shouldStripLeadingAndTrailingColons() {
//  Arrange
        val sim = Sim("Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor))\n\n::It was a dark and stormy night.::")

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        assertEquals("Stripped line", "It was a dark and stormy night", utterances[3])
    }

    @Test
    fun shouldHandleLineBreaksMidSentence() {
        //  Arrange
        val sim = Sim(
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.\nBill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        println(utterances)
        assertEquals("Number of Utterances", 7, utterances.size)
    }

    @Test
    fun shouldHandleEndOfSentenceOnNextLine(){
        //  Arrange
        val sim = Sim(
                "Test Sim", "Kevin", System.currentTimeMillis(),
                "((Corridor - USS Imaginary))\n\nIt was a\ndark and stormy night.  Bill\nhad just arrived."
        )

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        println(utterances)
        assertEquals("Number of Utterances", 5, utterances.size)
    }

    @Test
    fun shouldHandleMultilineSentence(){
        //  Arrange
        val sim = Sim(
                "Test Sim", "Kevin", System.currentTimeMillis(),
                "((Corridor - USS Imaginary))\n\nIt was a\n bleak, dark and\nstormy night.  Bill\nhad just arrived.  His rage was blinding."
        )

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        println(utterances)
        assertEquals("Number of Utterances", 6, utterances.size)
        assertEquals("Split sentence", "It was a bleak, dark and stormy night", utterances[3])
    }

    @Test
    fun shouldHandleIncompleteSentence(){
        //  Arrange
        val sim = Sim(
                "Test Sim", "Kevin", System.currentTimeMillis(),
                "((Corridor - USS Imaginary))\n\nIt was a\n bleak, dark and\nstormy night"
        )

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        println(utterances)
        assertEquals("Number of Utterances", 4, utterances.size)
        assertEquals("Split sentence", "It was a bleak, dark and stormy night", utterances[3])
    }

    @Test
    fun shouldCountUtterancesCorrectly(){
        val sim = Sim(
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Hypothetical))\n\nIt was a dark and stormy night.  Bill had\njust arrived.\n\nJim:  Bill, I didn't expect you!!!\n\nBill:  Hahaha!"
        )

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        println(utterances)
        assertEquals("Number of Utterances", 7, utterances.size)

    }

}