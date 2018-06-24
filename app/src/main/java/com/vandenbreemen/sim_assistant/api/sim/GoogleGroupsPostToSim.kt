package com.vandenbreemen.sim_assistant.api.sim

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Uid
import io.objectbox.relation.ToOne

/**
 *
 * @author kevin
 */
@Entity
@Uid(7115613438066029554L)
class GoogleGroupsPostToSim {

        @Id
        var id: Long = 0

        lateinit var sim:ToOne<Sim>

        lateinit var cachedGoogleGroupsPost:ToOne<CachedGoogleGroupsPost>

}