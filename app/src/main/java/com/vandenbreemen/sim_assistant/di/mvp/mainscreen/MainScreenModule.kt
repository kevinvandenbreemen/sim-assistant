package com.vandenbreemen.sim_assistant.di.mvp.mainscreen

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.UserSettingsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GooglePostCacheInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.post.simlist.SimListPresenterProviderImpl
import com.vandenbreemen.sim_assistant.mvp.impl.usersettings.UserSettingsRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import com.vandenbreemen.sim_assistant.mvp.usersettings.UserSettingsRepository
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule {

    @Provides
    fun providesMainScreenPresenter(activity: MainActivity, userSettingsRepository: UserSettingsRepository): MainScreenPresenter {
        return MainScreenPresenterImpl(MainScreenModelImpl(UserSettingsInteractorImpl(userSettingsRepository),
                GoogleGroupsInteractorImpl(activity.application as SimAssistantApp)
                ),
                activity )
    }

    @Provides
    fun providesSimListPresenterProvider(activity:MainActivity,
                                         userSettingsInteractor: UserSettingsInteractor,
                                         googleGroupsInteractor: GoogleGroupsInteractor,
                                         googleGroupCacheInteractor: GooglePostCacheInteractor
                                         ):SimListPresenterProvider{
        return SimListPresenterProviderImpl(
                activity.application as SimAssistantApp,
                userSettingsInteractor,
                googleGroupsInteractor,
                googleGroupCacheInteractor
        )
    }

    @Provides
    fun providesGoogleGroupsInteractor(activity:MainActivity):GoogleGroupsInteractor{
        return GoogleGroupsInteractorImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesGooglePostCacheInteractor(activity:MainActivity):GooglePostCacheInteractor{
        return GooglePostCacheInteractor(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesUserSettingsRepository(activity: MainActivity): UserSettingsRepository {
        return UserSettingsRepositoryImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesUserSettingsInteractor(userSettingsRepository: UserSettingsRepository): UserSettingsInteractor {
        return UserSettingsInteractorImpl(userSettingsRepository)
    }

}