package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.api.sim.*
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

    override fun referenceCachedPostToSim(cachedGoogleGroupsPost: CachedGoogleGroupsPost, sim: Sim) {
        var cacheRefToSim = GoogleGroupsPostToSim()
        cacheRefToSim.cachedGoogleGroupsPost.target = cachedGoogleGroupsPost
        cacheRefToSim.sim.target = sim
        simAssistantApp.boxStore.boxFor(GoogleGroupsPostToSim::class.java).put(cacheRefToSim)
    }

    override fun findCorrespondingSim(cachedGoogleGroupsPost: CachedGoogleGroupsPost):Sim? {
        var result = simAssistantApp.boxStore.boxFor(GoogleGroupsPostToSim::class.java).query()
                .equal(GoogleGroupsPostToSim_.cachedGoogleGroupsPostId, cachedGoogleGroupsPost.id)
                .eager(GoogleGroupsPostToSim_.sim)
                .build().findUnique()

        result?.let {
            return it.sim.target
        }

        return null
    }

    override fun cacheSim(simUrl: String, simContent: String):CachedGoogleGroupsPost {

        val toStore = CachedGoogleGroupsPost(simUrl, 0L, simContent)
        simAssistantApp.boxStore.boxFor(CachedGoogleGroupsPost::class.java).put(
                toStore
        )

        return toStore
    }


}