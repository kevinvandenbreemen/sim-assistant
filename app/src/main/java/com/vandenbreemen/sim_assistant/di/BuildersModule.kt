package com.vandenbreemen.sim_assistant.di

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.di.mvp.mainscreen.MainScreenModule
import com.vandenbreemen.sim_assistant.di.mvp.viewsim.ViewSimModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MainScreenModule::class])
    abstract fun buildMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ViewSimModule::class])
    abstract fun buildViewSimActivity(): ViewSimActivity

}