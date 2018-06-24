package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost
import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost_
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsCachedPostRepository

class GoogleGroupsCachedPostRepositoryImpl(private val simAssistantApp: SimAssistantApp) : GoogleGroupsCachedPostRepository {

    override fun retrieve(simUrl: String): CachedGoogleGroupsPost? {
        val result = simAssistantApp.boxStore.boxFor(CachedGoogleGroupsPost::class.java).query()
                .equal(CachedGoogleGroupsPost_.key, simUrl).build().find()
        if (result.size > 0) {
            return result[0]
        }
        return null
    }

    override fun cacheSim(simUrl: String, simContent: String) {
        simAssistantApp.boxStore.boxFor(CachedGoogleGroupsPost::class.java).put(
                CachedGoogleGroupsPost(simUrl, 0L, simContent)
        )
    }


}