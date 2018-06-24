package com.vandenbreemen.sim_assistant.di.mvp.mainscreen

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupRepository
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsCachedPostRepository
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsCachedPostRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.UserSettingsInteractorImpl
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
    fun providesMainScreenPresenter(activity: MainActivity, userSettingsRepository: UserSettingsRepository, googleGroupRepository: GoogleGroupRepository): MainScreenPresenter {
        return MainScreenPresenterImpl(MainScreenModelImpl(UserSettingsInteractorImpl(userSettingsRepository),
                GoogleGroupsInteractorImpl(googleGroupRepository)
                ),
                activity )
    }

    @Provides
    fun providesSimListPresenterProvider(activity:MainActivity,
                                         userSettingsInteractor: UserSettingsInteractor,
                                         googleGroupsInteractor: GoogleGroupsInteractor,
                                         googleGroupCacheInteractor: GoogleGroupsCachedPostRepository
                                         ):SimListPresenterProvider{
        return SimListPresenterProviderImpl(
                activity.application as SimAssistantApp,
                userSettingsInteractor,
                googleGroupsInteractor,
                googleGroupCacheInteractor
        )
    }

    @Provides
    fun providesGoogleGroupsInteractor(googleGroupRepository: GoogleGroupRepository): GoogleGroupsInteractor {
        return GoogleGroupsInteractorImpl(googleGroupRepository)
    }

    @Provides
    fun providesGoogleGroupsCachedPostRepository(activity: MainActivity): GoogleGroupsCachedPostRepository {
        return GoogleGroupsCachedPostRepositoryImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesUserSettingsRepository(activity: MainActivity): UserSettingsRepository {
        return UserSettingsRepositoryImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesGoogleGroupsRepository(activity: MainActivity): GoogleGroupRepository {
        return GoogleGroupRepositoryImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesUserSettingsInteractor(userSettingsRepository: UserSettingsRepository): UserSettingsInteractor {
        return UserSettingsInteractorImpl(userSettingsRepository)
    }

}