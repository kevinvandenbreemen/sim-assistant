package com.vandenbreemen.sim_assistant.api.sim

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 *
 * @author kevin
 */
@Entity
data class Tag(
        @Id
        var id:Long,
        val name:String)