package com.vandenbreemen.sim_assistant.di.mvp.viewsim

import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.impl.viewsim.ViewSimModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.viewsim.ViewSimPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import dagger.Module
import dagger.Provides

@Module
class ViewSimModule {

    @Provides
    fun providesViewSimPresenter(activity:ViewSimActivity, ttsInteractor: TTSInteractor):ViewSimPresenter{
        val parcelableArray = activity.intent.getParcelableArrayExtra(ViewSimActivity.PARM_SIMS)

        return ViewSimPresenterImpl(
                ViewSimModelImpl(parcelableArray.map { item -> item as Sim }.toTypedArray()),
                activity,
                ttsInteractor
        )
    }

}