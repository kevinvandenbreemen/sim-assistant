package com.vandenbreemen.sim_assistant.app

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.util.Log
import com.vandenbreemen.sim_assistant.MyObjectBox
import com.vandenbreemen.sim_assistant.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasFragmentInjector
import io.objectbox.BoxStore
import javax.inject.Inject

open class SimAssistantApp : Application(), HasActivityInjector, HasFragmentInjector {


    companion object {
        const val TAG = "SimAssistApp"
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingFragmentINjector: DispatchingAndroidInjector<Fragment>

    val boxStore: BoxStore by lazy {
        Log.d(TAG, "Initializing ObjectBox...")
        MyObjectBox.builder().androidContext(this).build()
    }

    open fun isInUnitTest(): Boolean {
        return false
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

    override fun fragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingFragmentINjector
    }
}