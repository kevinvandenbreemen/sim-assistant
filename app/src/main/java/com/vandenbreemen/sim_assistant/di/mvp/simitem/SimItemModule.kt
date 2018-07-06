package com.vandenbreemen.sim_assistant.di.mvp.simitem

import com.vandenbreemen.sim_assistant.adapters.SimAdapter
import com.vandenbreemen.sim_assistant.fragments.SimListFragment
import com.vandenbreemen.sim_assistant.mvp.impl.simitem.SimItemPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.simitem.SimItemPresenter
import dagger.Module
import dagger.Provides

@Module
class SimItemModule {

    @Provides
    fun providesSimAdapter(simItemPresenter: SimItemPresenter): SimAdapter {
        return SimAdapter(simItemPresenter)
    }

    @Provides
    fun providesSimItemPresenter(fragment: SimListFragment): SimItemPresenter {
        return SimItemPresenterImpl(fragment)
    }

}