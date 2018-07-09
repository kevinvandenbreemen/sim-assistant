package com.vandenbreemen.sim_assistant.di

import com.vandenbreemen.sim_assistant.MainActivity
import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.di.mvp.mainscreen.MainScreenModule
import com.vandenbreemen.sim_assistant.di.mvp.simitem.SimItemModule
import com.vandenbreemen.sim_assistant.di.mvp.tag.TagModule
import com.vandenbreemen.sim_assistant.di.mvp.tts.TTSInteractorModule
import com.vandenbreemen.sim_assistant.di.mvp.viewsim.ViewSimModule
import com.vandenbreemen.sim_assistant.fragments.SimListFragment
import com.vandenbreemen.sim_assistant.fragments.SimTagManagerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MainScreenModule::class])
    abstract fun buildMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ViewSimModule::class, TTSInteractorModule::class])
    abstract fun buildViewSimActivity(): ViewSimActivity

    @ContributesAndroidInjector(modules = [SimItemModule::class])
    abstract fun buildSimListFragment(): SimListFragment

    @ContributesAndroidInjector(modules = [TagModule::class])
    abstract fun buildSimTagManagerFragment(): SimTagManagerFragment

}