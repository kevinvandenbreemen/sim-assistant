package com.vandenbreemen.sim_assistant.api.sim

class SimParser(val sim: Sim) {
    fun toUtterances(): List<String> {
        val utterances = mutableListOf<String>()
        utterances.add(sim.title)
        utterances.add("By ${sim.author}")

        val rawLines = "[\n]+".toRegex().split(sim.content)

        var sentence: String? = null
        rawLines.forEach { str ->
            val line = str.trim()

            //  Setting
            val settingRegex = "[(]+([^)]+)[)]+".toRegex()
            if (settingRegex.matches(line)) {
                utterances.add(settingRegex.matchEntire(line)!!.groupValues[1])
                return@forEach
            }

            val sentenceRegex = "[.!]+".toRegex()
            if (sentenceRegex.containsMatchIn(line)) {
                var betweenBreaks = sentenceRegex.split(line).filter { str -> !str.isBlank() }
                if (sentence != null) {
                    sentence = "$sentence ${betweenBreaks.get(0)}"
                    utterances.add(sentence!!)
                    sentence = null
                    if (betweenBreaks.size > 1) {
                        betweenBreaks = betweenBreaks.subList(1, betweenBreaks.size)
                    } else {
                        return@forEach
                    }
                }
                utterances.addAll(betweenBreaks)
                return@forEach
            } else {
                if (sentence.isNullOrBlank()) {
                    sentence = line
                } else {
                    sentence += " $line"
                }
                return@forEach
            }

            utterances.add(line)
        }

        sentence?.let { it ->
            utterances.add(it)
        }

        return utterances
    }
}