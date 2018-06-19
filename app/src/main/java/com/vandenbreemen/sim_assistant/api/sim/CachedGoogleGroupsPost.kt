package com.vandenbreemen.sim_assistant.api.sim

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
@Entity(tableName = "cached_sim")
data class CachedGoogleGroupsPost(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "key_url")
        val key:String,

        val content:String

) {
}