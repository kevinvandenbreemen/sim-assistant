package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import android.arch.persistence.room.Room
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.DAO
import com.vandenbreemen.sim_assistant.data.UserSettings
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io
import java.util.function.Consumer

class UserSettingsInteractorImpl(private val application: SimAssistantApp) : UserSettingsInteractor {

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

    override fun getUserSettings(): Single<UserSettings> {
        return Single.create(SingleOnSubscribe<UserSettings> {

            doWithDatabase(Consumer{ dao->
                var loadedSettings = dao.getUserSettings()
                if(loadedSettings == null){
                    loadedSettings = UserSettings(1, -1)
                    dao.storeUserSettings(loadedSettings)
                }

                it.onSuccess(loadedSettings)
            })

        })
    }

    override fun updateUserSettings(updatedSettings: UserSettings): Single<Unit> {
        return Single.create(SingleOnSubscribe<Unit> {
            doWithDatabase(Consumer { dao ->
                dao.updateUserSettings(updatedSettings)
                it.onSuccess(Unit)
            })

        }).subscribeOn(io())
    }
}