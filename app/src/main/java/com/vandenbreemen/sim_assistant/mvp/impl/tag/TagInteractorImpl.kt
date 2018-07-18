package com.vandenbreemen.sim_assistant.mvp.impl.tag

import android.util.Log
import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.TagInteractor
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

class TagInteractorImpl(val repository: TagRepository) : TagInteractor{
    override fun searchTags(tagNameCriteria: String): Single<List<Tag>> {
        return Single.create(SingleOnSubscribe<List<Tag>> {
            it.onSuccess(repository.searchTag(tagNameCriteria))
        }).subscribeOn(io())
    }

    companion object {
        val TAG = TagInteractorImpl::class.java.simpleName
    }

    override fun addTag(name: String): Completable {
        return Completable.create(CompletableOnSubscribe {emitter->
            try{
                repository.addTag(name)
                emitter.onComplete()
            }
            catch(error:ApplicationError){
                Log.e(TAG, "Error adding tag", error)
                emitter.onError(error)
            }
        }).subscribeOn(io())
    }

    override fun getTags(): Single<List<Tag>> {
        return Single.create(SingleOnSubscribe<List<Tag>> {emitter->
            emitter.onSuccess(repository.getTags())
        }).subscribeOn(io())
    }

}
