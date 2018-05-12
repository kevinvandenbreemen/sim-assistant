package com.vandenbreemen.sim_assistant.mvp.mainscreen

enum class SimSource {

    GOOGLE_GROUP

    ;

    fun getId(): Long {
        return when (this) {
            GOOGLE_GROUP -> 1L
        }
    }

}
