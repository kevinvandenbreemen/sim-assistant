package com.vandenbreemen.sim_assistant.mvp.impl.post

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Sim_
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.post.SimRepository
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

/**
 *
 * @author kevin
 */
class SimRepositoryImpl(val app:SimAssistantApp):SimRepository {
    override fun store(sim: Sim): Completable {
        return Completable.create(CompletableOnSubscribe {
            app.boxStore.boxFor(Sim::class.java).put(sim)
            it.onComplete()
        }).subscribeOn(io())
    }

    override fun load(id: Long): Single<Sim> {
        return Single.create(SingleOnSubscribe<Sim> {emitter->
            val sim = app.boxStore.boxFor(Sim::class.java).query()
                    .equal(Sim_.__ID_PROPERTY, id).build().findUnique()

            sim?.let {
                emitter.onSuccess(it)
            }?:run {
               emitter.onError(ApplicationError("No Sim with ID=$id"))
            }
        }).subscribeOn(io())
    }


}