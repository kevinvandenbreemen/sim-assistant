package com.vandenbreemen.sim_assistant.mvp.mainscreen

import com.vandenbreemen.sim_assistant.data.UserSettings
import io.reactivex.Single

interface MainScreenPresenter {

    fun start()

}

interface UserSettingsInteractor{

    fun getUserSettings(): Single<UserSettings>

}