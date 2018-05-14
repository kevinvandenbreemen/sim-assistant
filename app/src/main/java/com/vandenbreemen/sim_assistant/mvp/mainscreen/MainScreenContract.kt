package com.vandenbreemen.sim_assistant.mvp.mainscreen

import com.vandenbreemen.sim_assistant.data.UserSettings
import io.reactivex.Completable
import io.reactivex.Single

interface MainScreenView{
    abstract fun showSimSourceSelector(simSources: List<SimSource>)

}

interface MainScreenPresenter {

    fun start():Completable

}

interface UserSettingsInteractor{

    fun getUserSettings(): Single<UserSettings>
    fun updateUserSettings(updatedSettings: UserSettings): Single<Unit>

}

interface MainScreenModel {

    fun checkShouldPromptUserForSimSource(): Single<Boolean>
    fun getPossibleSimSources(): List<SimSource>
    fun setSimSource(simSource: SimSource): Completable

}