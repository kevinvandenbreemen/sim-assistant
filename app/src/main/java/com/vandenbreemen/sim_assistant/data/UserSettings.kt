package com.vandenbreemen.sim_assistant.data

import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
data class UserSettings(


        @Id
        var id: Long,
        val dataSource:Long
)