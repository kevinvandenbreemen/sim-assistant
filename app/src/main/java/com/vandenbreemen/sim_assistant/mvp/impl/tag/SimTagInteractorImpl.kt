package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagInteractor
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

class SimTagInteractorImpl(val tagRepository: TagRepository) : SimTagInteractor {
    override fun filterEmptyTags(tags: List<Tag>): Single<List<Tag>> {
        return Single.create(SingleOnSubscribe<List<Tag>> { emitter ->
            emitter.onSuccess(
                    tags.filter { tag -> tagRepository.hasSims(tag) }
            )
        }).subscribeOn(io())
    }

    override fun getSims(tag: Tag): Single<List<Sim>> {
        return Single.create(SingleOnSubscribe<List<Sim>> {
            it.onSuccess(tagRepository.getSims(tag))
        }).subscribeOn(io())
    }

    override fun toggleTag(sim: Sim, tag: Tag): Completable {
        return Completable.create(CompletableOnSubscribe {
            if (tagRepository.hasTag(sim, tag)) {
                tagRepository.removeTag(sim, tag)
                it.onComplete()
                return@CompletableOnSubscribe
            }
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