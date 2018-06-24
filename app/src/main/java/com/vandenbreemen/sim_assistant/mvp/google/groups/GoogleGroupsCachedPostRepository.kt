package com.vandenbreemen.sim_assistant.mvp.google.groups

import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost

interface GoogleGroupsCachedPostRepository {

    fun retrieve(cacheKey: String): CachedGoogleGroupsPost?

    fun cacheSim(cacheKey: String, simContent: String)

}