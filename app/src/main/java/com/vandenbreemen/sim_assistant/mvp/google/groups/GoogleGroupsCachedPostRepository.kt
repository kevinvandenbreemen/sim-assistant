package com.vandenbreemen.sim_assistant.mvp.google.groups

import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost
import com.vandenbreemen.sim_assistant.api.sim.Sim

interface GoogleGroupsCachedPostRepository {

    fun retrieve(cacheKey: String): CachedGoogleGroupsPost?

    fun cacheSim(cacheKey: String, simContent: String):CachedGoogleGroupsPost

    fun referenceCachedPostToSim(cachedGoogleGroupsPost: CachedGoogleGroupsPost, sim:Sim)

    fun findCorrespondingSim(cachedGoogleGroupsPost: CachedGoogleGroupsPost):Sim?

}