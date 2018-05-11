package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class MainScreenModelImplTest {

    lateinit var mainScreenModel: MainScreenModel

    @Before
    fun setup() {
        mainScreenModel = MainScreenModelImpl(UserSettingsInteractorImpl(RuntimeEnvironment.application))
    }

    @Test
    fun shouldRequireSelectingSimSourceOnFirstAppUse() {
        val requiresPrompting = mainScreenModel.checkShouldPromptUserForSimSource().blockingGet()
        assertTrue("Needs prompting", requiresPrompting)
    }

}