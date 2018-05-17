package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import android.arch.persistence.room.Room
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.DAO
import com.vandenbreemen.sim_assistant.data.GoogleGroup
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
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
class GoogleGroupsInteractorImpl(private val application: SimAssistantApp) :GoogleGroupsInteractor {

    private fun doWithDatabase(function: Consumer<DAO>){

        val builder = Room.databaseBuilder(application.applicationContext,
                AppDatabase::class.java, "test-database")

        if (application.isInUnitTest()) {
            builder.allowMainThreadQueries()
        }

        val database = builder.build()

        val dao = database.dao()

        try{
            function.accept(dao)
        }
        finally{
            database.close()
        }
    }

    override fun addGoogleGroup(groupName: String): Completable {
        return Completable.fromAction( {
            doWithDatabase(Consumer { dao->
                val googleGroup = GoogleGroup(null, groupName)
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