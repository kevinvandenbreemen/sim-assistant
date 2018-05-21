package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.PostedSim
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListModel
import io.reactivex.Observable

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class SimListModelImpl(val postRepository: PostRepository): SimListModel {
    override fun getSimList(): Observable<PostedSim> {
        return postRepository.getPosts()
    }
}