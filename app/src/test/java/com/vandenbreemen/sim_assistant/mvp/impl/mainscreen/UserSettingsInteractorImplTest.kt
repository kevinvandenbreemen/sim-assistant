package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class UserSettingsInteractorImplTest{

    lateinit var userSettingsInteractor: UserSettingsInteractor

    @Before
    fun setup(){
        userSettingsInteractor = UserSettingsInteractorImpl(RuntimeEnvironment.application)
    }

    @Test
    fun shouldGenerateAndReturnUserSettingsOnFirstCall(){
        val userSettings = userSettingsInteractor.getUserSettings().blockingGet()
        assertNotNull("User Settings", userSettings)
    }

    @Test
    fun simSourceShouldBeUnknownByDefault(){
        val userSettings = userSettingsInteractor.getUserSettings().blockingGet()
        assertEquals("Unknown source", -1L, userSettings.dataSource)
    }

}