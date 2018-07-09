package com.vandenbreemen.sim_assistant.mvp.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Tag
import io.reactivex.Completable
import io.reactivex.Single

interface SimTagManagerPresenter {
    fun addTag(name: String)

}

interface SimTagManagerView {
    fun listTags(tags: List<Tag>)
    fun showError(applicationError: ApplicationError)
}

interface TagInteractor{
    fun addTag(name:String): Completable
    fun getTags():Single<List<Tag>>
}

interface TagRepository{
    fun addTag(name:String)
    fun getTags():List<Tag>
}