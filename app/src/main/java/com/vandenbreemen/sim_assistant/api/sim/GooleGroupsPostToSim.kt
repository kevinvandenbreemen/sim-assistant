package com.vandenbreemen.sim_assistant.api.sim

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 *
 * @author kevin
 */
@Entity
class GooleGroupsPostToSim {

        @Id
        var id: Long = 0

        lateinit var sim:ToOne<Sim>

        lateinit var cachedGoogleGroupsPost:ToOne<CachedGoogleGroupsPost>

}