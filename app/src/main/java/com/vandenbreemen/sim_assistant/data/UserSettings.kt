package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.objectbox.annotation.Id

@Entity
@io.objectbox.annotation.Entity
data class UserSettings(

        @PrimaryKey(autoGenerate = false)
        @Id
        var id: Long,
        val dataSource:Long
)