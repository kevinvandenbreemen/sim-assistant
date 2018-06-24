package com.vandenbreemen.sim_assistant.mvp.post

import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Completable
import io.reactivex.Single

/**
 *
 * @author kevin
 */
interface SimRepository {
    fun store(sim: Sim): Completable
    fun load(id: Long): Single<Sim>


}