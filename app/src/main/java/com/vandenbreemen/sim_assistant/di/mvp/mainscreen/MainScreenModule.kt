package com.vandenbreemen.sim_assistant.di.mvp.mainscreen

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.UserSettingsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule {

    @Provides
    fun providesMainScreenPresenter(activity:MainActivity):MainScreenPresenter{
        return MainScreenPresenterImpl(MainScreenModelImpl(UserSettingsInteractorImpl(activity.application as SimAssistantApp)),
                activity )
    }

}