package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
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

    @Mock
    lateinit var view: SimTagManagerView

    @Before
    fun setup(){

        RxJavaPlugins.setIoSchedulerHandler { mainThread() }

        sim = Sim(0, "kevin", "Kevin", 123L,"b")
        this.simTagManagerPresenter = SimTagManagerPresenterImpl(
                TagInteractorImpl(TagRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)),
                view)
    }

    @Test
    fun shouldAddTag(){

        //  Act
        simTagManagerPresenter.addTag("Test")

        //  Assert
        verify(view).listTags(listOf(Tag(1, "Test")))
    }

}