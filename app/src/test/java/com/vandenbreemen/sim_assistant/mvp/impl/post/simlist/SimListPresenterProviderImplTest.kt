package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsCachedPostRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.UserSettingsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.usersettings.UserSettingsRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.awaitility.Awaitility.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowLog
import java.util.concurrent.TimeUnit

/**
 * <h2>Intro</h2>
 *
 *
 * <h2>Other Details</h2>
 *
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class SimListPresenterProviderImplTest{

    lateinit var simListPresenterProvider: SimListPresenterProvider

    lateinit var simListView:SimListView

    lateinit var simList:MutableList<Sim>

    @Before
    fun setup(){

        simList = mutableListOf()

        simListView = object:SimListView{
            override fun deselectSim(sim: Sim) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun viewSim(sim: Sim) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun addSimItem(sim: Sim) {
                simList.add(sim)
            }

            override fun displayViewSelectedSimsOption() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun selectSim(sim: Sim) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun hideViewSelectedSimsOption() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun viewSelectedSims(sims:List<Sim>){

            }
        }

        val app = RuntimeEnvironment.application as SimAssistantApp

        ShadowLog.stream = System.out
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
        simListPresenterProvider = SimListPresenterProviderImpl(
                app,
                UserSettingsInteractorImpl(UserSettingsRepositoryImpl(app)),
                GoogleGroupsInteractorImpl(app),
                GoogleGroupsCachedPostRepositoryImpl(app)
        )
    }

    @Test
    fun shouldGeneratePresenterForGoogleGroup(){

        val mainScreenPresenter = MainScreenPresenterImpl(
                MainScreenModelImpl(UserSettingsInteractorImpl(
                        UserSettingsRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)),
                        GoogleGroupsInteractorImpl(RuntimeEnvironment.application as SimAssistantApp)
                ),
                mock(MainScreenView::class.java)
        )

        mainScreenPresenter.start().blockingAwait()

        mainScreenPresenter.setGoogleGroupName("sb118-apollo")

        val simListPresenter = simListPresenterProvider.getSimListPresenter().blockingGet()
        simListPresenter.start(simListView)

        await().atMost(5, TimeUnit.SECONDS).until { !simList.isEmpty() }
        println("Sim List:  $simList")
    }

}