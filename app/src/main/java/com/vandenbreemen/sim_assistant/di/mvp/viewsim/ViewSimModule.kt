package com.vandenbreemen.sim_assistant.di.mvp.viewsim

import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.impl.viewsim.ViewSimModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.viewsim.ViewSimPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import dagger.Module
import dagger.Provides

@Module
class ViewSimModule {

    @Provides
    fun providesViewSimPresenter(activity:ViewSimActivity):ViewSimPresenter{
        return ViewSimPresenterImpl(
                ViewSimModelImpl(activity.intent.getParcelableArrayExtra(ViewSimActivity.PARM_SIMS) as Array<Sim>),
                activity
        )
    }

}