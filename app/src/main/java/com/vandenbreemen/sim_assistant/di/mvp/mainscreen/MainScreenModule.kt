package com.vandenbreemen.sim_assistant.di.mvp.mainscreen

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.UserSettingsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule {

    @Provides
    fun providesMainScreenPresenter(activity:MainActivity):MainScreenPresenter{
        return MainScreenPresenterImpl(MainScreenModelImpl(UserSettingsInteractorImpl(activity.application)),
                object: MainScreenView{
                    override fun showSimSourceSelector(simSources: List<SimSource>) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                }
                )
    }

}