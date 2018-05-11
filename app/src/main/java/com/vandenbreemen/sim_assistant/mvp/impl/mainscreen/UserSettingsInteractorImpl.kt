package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import android.app.Application
import android.arch.persistence.room.Room
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.UserSettings
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

class UserSettingsInteractorImpl(private val application: Application) :UserSettingsInteractor {
    override fun getUserSettings(): Single<UserSettings> {
        return Single.create(SingleOnSubscribe<UserSettings> {
            val database = Room.databaseBuilder(application.applicationContext,
                    AppDatabase::class.java, "test-database").build()

            val dao = database.dao()

            var loadedSettings = dao.getUserSettings()
            if(loadedSettings == null){
                loadedSettings = UserSettings(1, -1)
                dao.storeUserSettings(loadedSettings)
            }

            it.onSuccess(loadedSettings)
        }).subscribeOn(io())
    }
}