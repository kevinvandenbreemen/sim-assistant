package com.vandenbreemen.sim_assistant.mvp.google.groups

import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost

interface GoogleGroupsCachedPostRepository {

    fun retrieve(simUrl: String): CachedGoogleGroupsPost?

    fun cacheSim(simUrl: String, simContent: String)

}