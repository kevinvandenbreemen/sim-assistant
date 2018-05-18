package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName="google_group")
data class GoogleGroup(
        @PrimaryKey(autoGenerate = false)
        val groupName: String) {

}
