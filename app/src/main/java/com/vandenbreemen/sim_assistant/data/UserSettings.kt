package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class UserSettings(

        @PrimaryKey(autoGenerate = false)
        val id:Long,
        val dataSource:Long
)