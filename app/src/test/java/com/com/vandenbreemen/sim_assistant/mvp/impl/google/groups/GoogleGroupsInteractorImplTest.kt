package com.com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsInteractor
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsInteractorImpl
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class GoogleGroupsInteractorImplTest {

    lateinit var googleGroupsInteractor: GoogleGroupsInteractor

    @Before
    fun setup(){
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
        googleGroupsInteractor = GoogleGroupsInteractorImpl(RuntimeEnvironment.application as SimAssistantApp)
    }

    @Test
    fun shouldAddGoogleGroup(){
        googleGroupsInteractor.addGoogleGroup("test-group").blockingAwait()

        val groupsList = googleGroupsInteractor.getGoogleGroups().blockingGet()
        assertFalse("Google groups", groupsList.isEmpty())
        assertEquals("Group name", "test-group", groupsList.get(0).groupName)

    }

}