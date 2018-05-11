package com.vandenbreemen.sim_assistant.di.mvp.mainscreen

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule {

    @Provides
    fun providesMainScreenPresenter(activity:MainActivity):MainScreenPresenter{
        return MainScreenPresenterImpl()
    }

}