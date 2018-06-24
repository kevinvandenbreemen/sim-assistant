package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.GoogleGroup
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GoogleGroupRepositoryImplTest {

    lateinit var googleGroupRepository: GoogleGroupRepository

    @Before
    fun setup() {
        googleGroupRepository = GoogleGroupRepositoryImpl(
                RuntimeEnvironment.application as SimAssistantApp
        )
    }

    @Test
    fun shouldStoreGoogleGroup() {
        val googleGroup = GoogleGroup(0L, "kevin-group")
        googleGroupRepository.addGoogleGroup(googleGroup)

        assertFalse("Groups", googleGroupRepository.getGoogleGroups().isEmpty())
    }

    @Test
    fun shouldNotAllowStoringSameGroupName(){
        var googleGroup = GoogleGroup(0L, "kevin-group")
        googleGroupRepository.addGoogleGroup(googleGroup)
        googleGroup = GoogleGroup(0L, "kevin-group")
        googleGroupRepository.addGoogleGroup(googleGroup)

        assertEquals("Unique Name", 1, googleGroupRepository.getGoogleGroups().size)
    }

}