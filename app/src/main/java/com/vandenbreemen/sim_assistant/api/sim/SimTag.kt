package com.vandenbreemen.sim_assistant.api.sim

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class SimTag(
        @Id
        var id: Long,
        val simId: Long,
        val tagId: Long
)