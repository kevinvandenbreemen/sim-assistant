package com.vandenbreemen.sim_assistant.api.sim

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient

/**
 *
 * @author kevin
 */
@Entity
data class Tag(
        @Id
        var id: Long,
        val name: String,

        @Transient
        val selected: Boolean = false){
    constructor():
        this(0,"")

}