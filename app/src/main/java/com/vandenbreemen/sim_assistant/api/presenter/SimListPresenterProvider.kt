package com.vandenbreemen.sim_assistant.api.presenter

import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import io.reactivex.Single

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
interface SimListPresenterProvider {
    fun getSimListPresenter(): Single<SimListPresenter>
}