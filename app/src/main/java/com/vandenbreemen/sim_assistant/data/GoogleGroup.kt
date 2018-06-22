package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.objectbox.annotation.Id

@Entity(tableName="google_group")
@io.objectbox.annotation.Entity
data class GoogleGroup(
        @Id
        var id: Long,
        @PrimaryKey(autoGenerate = false)
        val groupName: String) {

}
