package com.vandenbreemen.sim_assistant.mvp.impl.usersettings

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.usersettings.UserSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class UserSettingsRepositoryImplTest {

    lateinit var userSettingsRepository: UserSettingsRepository

    @Before
    fun setup() {
        this.userSettingsRepository = UserSettingsRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
    }

    @Test
    fun shouldAutoGenerateUserSettings() {
        val userSettings = userSettingsRepository.getUserSettings().blockingGet()
        assertNotNull("User Settings", userSettings)
    }

    @Test
    fun shouldGenerateDefaultDataSource() {
        val userSettings = userSettingsRepository.getUserSettings().blockingGet()
        assertEquals("default data src", -1,
                userSettings.dataSource)
    }

    @Test
    fun shouldUpdateUserSettings() {
        val userSettings = userSettingsRepository.getUserSettings().blockingGet()
        val updatedSettings = userSettings.copy(dataSource = 1)

        userSettingsRepository.updateUserSettings(updatedSettings).blockingAwait()
        val stored = userSettingsRepository.getUserSettings().blockingGet()
        assertEquals("Updated", 1, stored.dataSource)
    }

}