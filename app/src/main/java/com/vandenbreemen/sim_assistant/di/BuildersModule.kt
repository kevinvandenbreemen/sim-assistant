package com.vandenbreemen.sim_assistant.di

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.di.mvp.mainscreen.MainScreenModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MainScreenModule::class])
    abstract fun buildMainActivity(): MainActivity

}