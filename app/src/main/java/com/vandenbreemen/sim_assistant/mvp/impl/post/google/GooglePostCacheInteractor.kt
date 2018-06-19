package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.DatabaseInteractor
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.schedulers.Schedulers.io
import java.util.function.Consumer

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class GooglePostCacheInteractor(application: SimAssistantApp) : DatabaseInteractor(application) {

    companion object {
        val NO_CACHE_HIT = CachedGoogleGroupsPost("", "")
    }

    fun retrieve(simUrl: String): CachedGoogleGroupsPost {

        var returnSim = NO_CACHE_HIT
        doWithDatabase(Consumer { dao->

            val cachedSim = dao.getCachedSim(simUrl)
            if(cachedSim != null){
                returnSim = cachedSim
            }
        })

        return returnSim

    }

    fun cacheSim(simUrl: String, simContent: String){
        Completable.create(CompletableOnSubscribe {emitter->
            doWithDatabase(Consumer { dao->
                dao.storeCachedSim(CachedGoogleGroupsPost(simUrl, simContent))
                emitter.onComplete()
            })
        }).subscribeOn(io()).subscribe()
    }

}