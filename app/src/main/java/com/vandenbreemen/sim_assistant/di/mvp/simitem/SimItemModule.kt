package com.vandenbreemen.sim_assistant.di.mvp.simitem

import com.vandenbreemen.sim_assistant.adapters.SimAdapter
import com.vandenbreemen.sim_assistant.fragments.SimListFragment
import dagger.Module
import dagger.Provides

@Module
class SimItemModule {

    @Provides
    fun providesSimAdapter(fragment: SimListFragment): SimAdapter {
        return SimAdapter()
    }

}