package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.sim.CachedSim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.DatabaseInteractor
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
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
        val NO_CACHE_HIT = CachedSim("", "")
    }

    fun retrieve(simUrl: String): Single<CachedSim?> {
        return Single.create(SingleOnSubscribe<CachedSim> { emitter ->
            doWithDatabase(Consumer { dao->

                val cachedSim = dao.getCachedSim(simUrl)
                if(cachedSim == null){
                    emitter.onSuccess(NO_CACHE_HIT)
                }
            })
        }).subscribeOn(io())

    }

}