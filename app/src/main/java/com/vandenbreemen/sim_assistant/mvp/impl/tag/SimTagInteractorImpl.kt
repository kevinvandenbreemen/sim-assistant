package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.post.SimRepository
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagInteractor
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

class SimTagInteractorImpl(val tagRepository: TagRepository, val simRepository: SimRepository) : SimTagInteractor {
    override fun addTag(sim: Sim, tag: Tag): Completable {
        return Completable.create(CompletableOnSubscribe {
            tagRepository.tagSim(sim, tag)
            it.onComplete()
        }).subscribeOn(io())

    }

    override fun getTags(sim: Sim): Single<List<Tag>> {
        return Single.create(SingleOnSubscribe<List<Tag>> { emitter ->
            val allTags = tagRepository.getTags()
            val simTags = tagRepository.getTags(sim)
            emitter.onSuccess(allTags.map { tag ->
                simTags.find { simTag -> tag.id == simTag.id }?.let {
                    return@map tag.copy(true)
                } ?: run {
                    return@map tag
                }
            })
        }).subscribeOn(io())
    }
}