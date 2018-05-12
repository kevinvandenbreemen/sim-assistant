package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenModel
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.io

class MainScreenModelImpl(private val userSettingsInteractor: UserSettingsInteractor) : MainScreenModel {

    companion object {
        const val UNKNOWN_SIM_SOURCE = -1L
    }

    override fun checkShouldPromptUserForSimSource(): Single<Boolean> {
        return userSettingsInteractor.getUserSettings().flatMap { userSettings ->
            Single.just(userSettings.dataSource == UNKNOWN_SIM_SOURCE)
        }.subscribeOn(io())
    }

    override fun getPossibleSimSources(): List<SimSource> {
        return SimSource.values().asList()
    }


    override fun setSimSource(simSource: SimSource): Completable {
        return userSettingsInteractor.getUserSettings().flatMapCompletable { settings ->
            CompletableSource { completableObs ->
                val updatedSettings = settings.copy(dataSource = simSource.getId())
                userSettingsInteractor.updateUserSettings(updatedSettings)
                        .subscribe { it -> completableObs.onComplete() }

            }

        }
    }
}