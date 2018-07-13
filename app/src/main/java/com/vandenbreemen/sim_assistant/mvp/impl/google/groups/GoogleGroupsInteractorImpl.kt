package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import android.util.Log
import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.data.GoogleGroup
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupRepository
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GOOGLE_GROUPS_BASE_URL
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.SingleSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers.io
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class GoogleGroupsInteractorImpl(private val googleGroupRepository: GoogleGroupRepository) : GoogleGroupsInteractor {

    companion object {
        val TAG = "GoogleGroupsInteractor"
    }

    override fun googleGroupExists(groupName: String): Single<Boolean> {

        val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create()).build().create(GoogleGroupsApi::class.java)
        return googleGroupsApi.getRssFeed(groupName, 1).subscribeOn(io()).flatMap(Function<GoogleGroupsRSSFeed, SingleSource<Boolean>> { googleGroup ->
            SingleSource<Boolean> { observer ->
                observer.onSuccess(true)
            }
        }).onErrorResumeNext { error->
            Log.e(TAG, "Failed to determine if $groupName Group Exists", error)
            Single.just(false)
        }
    }

    override fun addGoogleGroup(groupName: String): Completable {
        return Completable.create {
            emitter ->
            if(groupName.isNullOrBlank()){
                emitter.onError(ApplicationError("Please specify group name"))
                return@create
            }

            val googleGroup = GoogleGroup(0L, groupName)
            googleGroupRepository.addGoogleGroup(googleGroup)
            emitter.onComplete()
        }.subscribeOn(io())
    }

    override fun getGoogleGroups(): Single<List<GoogleGroup>> {
        return Single.create(SingleOnSubscribe {emitter->
            emitter.onSuccess(googleGroupRepository.getGoogleGroups())
        })
    }
}