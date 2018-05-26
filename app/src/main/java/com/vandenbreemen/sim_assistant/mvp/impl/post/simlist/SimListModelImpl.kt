package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListModel
import io.reactivex.Observable
import java.util.*

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class SimListModelImpl(val postRepository: PostRepository): SimListModel {

    val selectedSims = mutableListOf<Sim>()

    override fun getSimList(): Observable<Sim> {
        return postRepository.getPosts()
    }

    override fun selectedSims(): List<Sim> {
        return Collections.unmodifiableList(selectedSims)
    }

    override fun hasSelectedSims(): Boolean {
        return selectedSims.isNotEmpty()
    }

    override fun selectSim(sim: Sim) {
        selectedSims.add(sim)
    }
}