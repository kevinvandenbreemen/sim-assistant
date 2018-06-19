package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.data.UserSettings
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import com.vandenbreemen.sim_assistant.mvp.usersettings.UserSettingsRepository
import io.reactivex.Single

class UserSettingsInteractorImpl(private val userSettingsRepository: UserSettingsRepository) : UserSettingsInteractor {

    override fun getUserSettings(): Single<UserSettings> {
        return userSettingsRepository.getUserSettings()
    }

    override fun updateUserSettings(updatedSettings: UserSettings): Single<Unit> {
        return userSettingsRepository.updateUserSettings(updatedSettings).toSingle {
            Unit
        }
    }
}