package com.vandenbreemen.sim_assistant.mvp.post

import com.vandenbreemen.sim_assistant.api.sim.PostedSim
import io.reactivex.Observable

interface PostRepository {
    fun getPosts(): Observable<PostedSim>
}