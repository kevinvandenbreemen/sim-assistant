package com.vandenbreemen.sim_assistant.mvp.impl.usersettings

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.UserSettings
import com.vandenbreemen.sim_assistant.mvp.usersettings.UserSettingsRepository
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

class UserSettingsRepositoryImpl(private val application: SimAssistantApp) : UserSettingsRepository {
    override fun getUserSettings(): Single<UserSettings> {
        val objectBox = application.boxStore.boxFor(UserSettings::class.java)

        return Single.create(SingleOnSubscribe<UserSettings> { emitter ->
            if (objectBox.all.isEmpty()) {
                val defaultSettings = UserSettings(0, -1)
                objectBox.put(defaultSettings)
            }
            emitter.onSuccess(objectBox.get(1))
        }).subscribeOn(io())
    }

    override fun updateUserSettings(updatedSettings: UserSettings): Completable {
        val objectBox = application.boxStore.boxFor(UserSettings::class.java)
        return Completable.create(CompletableOnSubscribe {
            objectBox.put(updatedSettings)
            it.onComplete()
        }).subscribeOn(io())
    }
}