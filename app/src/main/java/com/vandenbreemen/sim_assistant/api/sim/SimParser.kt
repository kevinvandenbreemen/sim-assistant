package com.vandenbreemen.sim_assistant.api.sim

class SimParser(val sim: Sim) {
    fun toUtterances(): List<String> {
        val utterances = mutableListOf<String>()
        utterances.add(sim.title)
        utterances.add("By ${sim.author}")

        val rawLines = "[\n]+".toRegex().split(sim.content)
        rawLines.forEach { str ->
            val line = str.trim()

            //  Setting
            val settingRegex = "[(]+([^)]+)[)]+".toRegex()
            if (settingRegex.matches(line)) {
                utterances.add(settingRegex.matchEntire(line)!!.groupValues[1])
            } else {
                utterances.add(line)
            }
        }



        return utterances
    }
}