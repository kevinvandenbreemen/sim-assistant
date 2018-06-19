package com.vandenbreemen.sim_assistant.mvp.usersettings

import com.vandenbreemen.sim_assistant.data.UserSettings
import io.reactivex.Completable
import io.reactivex.Single

interface UserSettingsRepository {

    fun getUserSettings(): Single<UserSettings>

    fun updateUserSettings(updatedSettings: UserSettings): Completable

}