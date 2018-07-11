package com.vandenbreemen.sim_assistant.mvp.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import io.reactivex.Completable
import io.reactivex.Single

interface SimTagManagerPresenter {
    fun addTag(name: String)
    fun start(sim: Sim)
    fun addTag(sim: Sim, tag: Tag)
}

interface SimTagManagerView {
    fun listTags(tags: List<Tag>)
    fun showError(applicationError: ApplicationError)
}

interface TagInteractor{
    fun addTag(name:String): Completable
    fun getTags():Single<List<Tag>>
}

interface SimTagInteractor {
    fun addTag(sim: Sim, tag: Tag): Completable
    fun getTags(sim: Sim): Single<List<Tag>>
}

interface TagRepository{
    fun addTag(name:String)
    fun getTags():List<Tag>
    fun tagSim(sim: Sim, tag: Tag)
    fun getTags(sim: Sim): List<Tag>
}