package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GoogleGroup(
        @PrimaryKey(autoGenerate = true)
        val id:Long?,
        val groupName: String) {

}
