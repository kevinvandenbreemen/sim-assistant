package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.mvp.mainscreen.UserSettingsInteractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    @Test
    fun shouldUpdateUserSettings(){
        var userSettings = userSettingsInteractor.getUserSettings().blockingGet()
        val updatedSettings = userSettings.copy(dataSource = 1L)
        userSettingsInteractor.updateUserSettings(updatedSettings).blockingGet()

        userSettings  = userSettingsInteractor.getUserSettings().blockingGet()
        assertEquals("Updated source", 1L, userSettings.dataSource)
    }

}