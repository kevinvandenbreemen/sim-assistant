package com.vandenbreemen.sim_assistant.di.mvp.mainscreen

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
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
import com.vandenbreemen.sim_assistant.mvp.impl.post.SimRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GOOGLE_GROUPS_BASE_URL
import com.vandenbreemen.sim_assistant.mvp.impl.post.simlist.SimListPresenterProviderImpl
import com.vandenbreemen.sim_assistant.mvp.impl.tag.SimTagInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.tag.TagInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.tag.TagRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.tag.TagSearchPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.usersettings.UserSettingsRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import com.vandenbreemen.sim_assistant.mvp.post.SimRepository
import com.vandenbreemen.sim_assistant.mvp.tag.*
import com.vandenbreemen.sim_assistant.mvp.usersettings.UserSettingsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

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
    fun provideSimRepository(activity: MainActivity):SimRepository{
        return SimRepositoryImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesGoogleGroupsApi():GoogleGroupsApi{
        return Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create()).build().create(GoogleGroupsApi::class.java)
    }

    @Provides
    fun providesSimListPresenterProvider(activity:MainActivity,
                                         googleGroupsApi: GoogleGroupsApi,
                                         userSettingsInteractor: UserSettingsInteractor,
                                         googleGroupsInteractor: GoogleGroupsInteractor,
                                         googleGroupCacheInteractor: GoogleGroupsCachedPostRepository,
                                         simRepository: SimRepository
                                         ):SimListPresenterProvider{
        return SimListPresenterProviderImpl(
                activity.application as SimAssistantApp,
                userSettingsInteractor,
                googleGroupsInteractor,
                googleGroupsApi,
                googleGroupCacheInteractor,
                simRepository
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

    @Provides
    fun providesTagSimSearchView(activity: MainActivity): TagSimSearchView {
        return activity
    }

    @Provides
    fun providesTagRepository(activity: MainActivity): TagRepository {
        return TagRepositoryImpl(activity.application as SimAssistantApp)
    }

    @Provides
    fun providesTagInteractor(tagRepository: TagRepository): TagInteractor {
        return TagInteractorImpl(tagRepository)
    }

    @Provides
    fun providesSimTagInteractor(tagRepository: TagRepository): SimTagInteractor {
        return SimTagInteractorImpl(tagRepository)
    }

    @Provides
    fun providesTagSimSearchRouter(activity: MainActivity): TagSimSearchRouter {
        return activity
    }

    @Provides
    fun providesTagSearchPresenter(tagInteractor: TagInteractor, simTagInteractor: SimTagInteractor,
                                   simTagSimSearchView: TagSimSearchView, simTagSimSearchRouter: TagSimSearchRouter): TagSimSearchPresenter {
        return TagSearchPresenterImpl(tagInteractor, simTagInteractor, simTagSimSearchView, simTagSimSearchRouter)
    }

}