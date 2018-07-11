package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.post.SimRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class SimTagManagerPresenterImplTest{

    @get:Rule
    val mockRule = MockitoJUnit.rule()

    lateinit var simTagManagerPresenter: SimTagManagerPresenter

    lateinit var sim : Sim

    lateinit var app:SimAssistantApp

    @Mock
    lateinit var view: SimTagManagerView

    @Before
    fun setup(){

        app = RuntimeEnvironment.application as SimAssistantApp

        RxJavaPlugins.setIoSchedulerHandler { mainThread() }

        sim = Sim(0, "kevin", "Kevin", 123L,"b")
        this.simTagManagerPresenter = SimTagManagerPresenterImpl(
                TagInteractorImpl(TagRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)),
                SimTagInteractorImpl(TagRepositoryImpl(app), SimRepositoryImpl(app)),
                view)


        //  Persist the sim
        app.boxStore.boxFor(Sim::class.java).put(sim)
    }

    @Test
    fun shouldAddTag(){

        //  Act
        simTagManagerPresenter.addTag("Test")

        //  Assert
        verify(view).listTags(listOf(Tag(1, "Test", false)))
    }

    @Test
    fun shouldShowErrorIfAddedBadTag(){
        //  Act
        simTagManagerPresenter.addTag("")

        //  Assert
        verify(view).showError(ApplicationError("Tag must have a name"))
    }

    @Test
    fun shouldTagSim(){
        //  Arrange
        val tag = Tag(0, "Test", false)
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        simTagManagerPresenter.addTag(sim, tag)

        //  Assert
        val expectedTag = Tag(1, "Test", true)
        verify(view).listTags(listOf(expectedTag))
    }

}