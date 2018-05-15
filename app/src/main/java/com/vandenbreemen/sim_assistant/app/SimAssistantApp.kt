package com.vandenbreemen.sim_assistant.app

import android.app.Activity
import android.app.Application
import com.vandenbreemen.sim_assistant.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class SimAssistantApp: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    private var isInUnitTest = false

    fun setInUnitTest() {
        isInUnitTest = true
    }

    fun isInUnitTest(): Boolean {
        return this.isInUnitTest
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}