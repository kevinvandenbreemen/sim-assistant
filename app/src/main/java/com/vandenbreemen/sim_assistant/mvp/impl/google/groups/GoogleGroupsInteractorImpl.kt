package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.GoogleGroup
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.DatabaseInteractor
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io
import java.util.function.Consumer

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class GoogleGroupsInteractorImpl(private val application: SimAssistantApp) :DatabaseInteractor(application), GoogleGroupsInteractor {

    override fun addGoogleGroup(groupName: String): Completable {
        return Completable.fromAction( {
            doWithDatabase(Consumer { dao->
                val googleGroup = GoogleGroup(groupName)
                dao.storeGoogleGroup(googleGroup)
            })
        }).subscribeOn(io())
    }

    override fun getGoogleGroups(): Single<List<GoogleGroup>> {
        return Single.create(SingleOnSubscribe {emitter->
            doWithDatabase(Consumer {
                doWithDatabase(Consumer {dao->
                    emitter.onSuccess(dao.getGoogleGroups())
                })

            })
        })
    }
}