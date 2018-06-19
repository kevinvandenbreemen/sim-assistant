package com.vandenbreemen.sim_assistant.app

import android.app.Activity
import android.app.Application
import android.util.Log
import com.vandenbreemen.sim_assistant.data.MyObjectBox
import com.vandenbreemen.sim_assistant.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.objectbox.BoxStore
import javax.inject.Inject

open class SimAssistantApp : Application(), HasActivityInjector {

    companion object {
        const val TAG = "SimAssistApp"
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    private lateinit var boxStore: BoxStore

    open fun isInUnitTest(): Boolean {
        return false
    }

    fun getBoxStore(): BoxStore {
        return this.boxStore
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Initializing ObjectBox...")
        this.boxStore = MyObjectBox.builder().androidContext(this).build()
        Log.d(TAG, "Succesfully initialized ObjectBox")

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}