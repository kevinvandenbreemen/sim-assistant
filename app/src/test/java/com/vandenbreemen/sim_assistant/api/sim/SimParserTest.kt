package com.vandenbreemen.sim_assistant.api.sim

import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class SimParserTest {

    @Test
    fun shouldRecognizeSettingAsSeparateUtterance() {

        //  Arrange
        val sim = Sim(0L,"Test Sim",
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
        val sim = Sim(0L,"Test Sim",
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
        val sim = Sim(0L,
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
        val sim = Sim(0L,
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
        val sim = Sim(0L,
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
        val sim = Sim(0L,
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
    fun shouldHandleLineWithTabsAndNoBreaks(){
        //  ....................................................
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                "((Corridor - USS Imaginary))\n\nIt was a\n\t.............................\nstormy night"
        )

        //  Act
        val parser = SimParser(sim)
        parser.toUtterances()
    }

    @Test
    fun shouldCountUtterancesCorrectly(){
        val sim = Sim(0L,
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

    @Test
    fun shouldNotSkipSentence() {
        val sim = Sim(0L,
                "Test Sim",
                "Kevin",
                System.currentTimeMillis(),
                ":: The sound of the QSD disengaging was enough of a shock to the pink haired engineer, that it took her a long moment before she could react.  The work around they'd pulled together was more likely to leave the QSD as scrap rather than to safely shut down.  And it should have been running for a lot longer to arrive.  With a confused look on her face, she began the process of determining what had gone wrong, only to see that the bridge had taken the ship out of slipstream intentionally.  However, before she could ask them why, they called her! ::"
        )

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()

        //  Assert
        println(utterances)
        assertEquals("Number of Utterances", 7, utterances.size)
        assertTrue("Skipped sentence contained", utterances.contains("The work around they'd pulled together was more likely to leave the QSD as scrap rather than to safely shut down"))
    }

    @Test
    fun shouldIgnoreBlankLines() {

        //  Arrange
        val sim = Sim(0L,"Test Sim", "Kevin",
                0L,
                ":: The sound of the QSD disengaging was enough of a shock to the pink haired engineer, that it took her a long moment before she could react.  The work around they'd pulled together was more likely to leave the QSD as scrap rather than to safely shut down.  And it should have been running for a lot longer to arrive.  With a confused look on her face, she began the process of determining what had gone wrong, only to see that the bridge had taken the ship out of slipstream intentionally.  However, before she could ask them why, they called her! ::\n" +
                        " \n" +
                        "St. John: =^= Lieutenant jaygee St. John to Engineering. =^=")

        //  Act
        val parser = SimParser(sim)
        val utterances = parser.toUtterances()
        println(utterances)

        //  Assert
        assertEquals("Parsed sentence ending in exclamation", "However, before she could ask them why, they called her", utterances[6])
    }

}