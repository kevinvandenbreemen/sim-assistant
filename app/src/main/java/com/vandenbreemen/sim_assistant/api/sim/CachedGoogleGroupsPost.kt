package com.vandenbreemen.sim_assistant.api.sim

import io.objectbox.annotation.Id

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
@io.objectbox.annotation.Entity
data class CachedGoogleGroupsPost(
        val key:String,

        @Id
        var id: Long,

        val content:String

) {
}