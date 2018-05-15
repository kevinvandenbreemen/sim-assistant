package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenModel
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import junit.framework.TestCase.assertFalse
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
        mainScreenModel = MainScreenModelImpl(UserSettingsInteractorImpl(RuntimeEnvironment.application as SimAssistantApp))
    }

    @Test
    fun shouldRequireSelectingSimSourceOnFirstAppUse() {
        val requiresPrompting = mainScreenModel.checkShouldPromptUserForSimSource().blockingGet()
        assertTrue("Needs prompting", requiresPrompting)
    }

    @Test
    fun shouldProvideListOfSimSourcesUserCanUse() {
        assertFalse("Sim sources", mainScreenModel.getPossibleSimSources().isEmpty())
    }

    @Test
    fun shouldStoreSimSource() {
        mainScreenModel.setSimSource(SimSource.GOOGLE_GROUP).blockingAwait()
        assertFalse("Needs prompting again",
                mainScreenModel.checkShouldPromptUserForSimSource().blockingGet())
    }

}