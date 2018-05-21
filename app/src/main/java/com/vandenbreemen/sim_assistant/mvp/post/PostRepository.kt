package com.vandenbreemen.sim_assistant.mvp.post

import com.vandenbreemen.sim_assistant.api.sim.Sim
import io.reactivex.Observable

interface PostRepository {
    fun getPosts(): Observable<Sim>
}