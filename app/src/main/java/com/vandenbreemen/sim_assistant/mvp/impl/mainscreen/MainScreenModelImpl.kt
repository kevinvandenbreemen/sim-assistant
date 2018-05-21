package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import android.util.Log
import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenModel
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers.io

class MainScreenModelImpl(private val userSettingsInteractor: UserSettingsInteractor,
                          private val googleGroupsInteractor: GoogleGroupsInteractor) : MainScreenModel {

    override fun addGoogleGroup(groupName: String):Completable {
        if(groupName.isBlank()){
            return Completable.error(ApplicationError("Please specify group name"))
        }
        return googleGroupsInteractor.googleGroupExists(groupName).flatMapCompletable {groupExists->
            CompletableSource { observer->
                if(groupExists){
                    Log.d(TAG, "Confirmed group $groupName exists.  Adding to the system")
                    googleGroupsInteractor.addGoogleGroup(groupName).subscribe {
                        Log.d(TAG, "Group $groupName added")
                        observer.onComplete()
                    }
                    return@CompletableSource
                }
                observer.onError(ApplicationError("$groupName:  No such Google Group"))
            }
        }
    }

    override fun getGoogleGroups(): Single<List<String>> {
        return googleGroupsInteractor.getGoogleGroups().flatMap {groups->
            Single.just(
                    groups.map { item->item.groupName }
            )
        }
    }

    companion object {
        const val UNKNOWN_SIM_SOURCE = -1L
        val TAG = "MainScreenModel"
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