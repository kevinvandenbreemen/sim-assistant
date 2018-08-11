package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsCachedPostRepository
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GooglePostContentLoader
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GooglePostRepository
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.SimRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers.io

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class SimListPresenterProviderImpl(
        val application:SimAssistantApp,
        private val userSettingsInteractor: UserSettingsInteractor,
        private val googleGroupsInteractor: GoogleGroupsInteractor,
        private val googleGroupsApi:GoogleGroupsApi,
        private val googleGroupsCachedPostRepository: GoogleGroupsCachedPostRepository,
        private val simRepository: SimRepository
                                   ):SimListPresenterProvider {
    override fun getSimListPresenter(): Single<SimListPresenter> {
        return userSettingsInteractor.getUserSettings().subscribeOn(io()).flatMap<SimListPresenter> { userSettings->

            Single.create(SingleOnSubscribe<SimListPresenter> {emitter->
                var repository:PostRepository? = null

                if(SimSource.GOOGLE_GROUP.getId() == userSettings.dataSource){
                    val groups = googleGroupsInteractor.getGoogleGroups().blockingGet()
                    repository = GooglePostRepository(groups[0].groupName,
                            googleGroupsApi,
                            GooglePostContentLoader(), googleGroupsCachedPostRepository,
                            simRepository
                            )
                }

                repository?.let {
                    emitter.onSuccess(SimListPresenterImpl(SimListModelImpl(it)))
                }

            })


        }.subscribeOn(io())
    }

    override fun getSimListPresenter(simList: List<Sim>): SimListPresenter {
        return SimListPresenterImpl(KnownSimListModel(simList))
    }


}