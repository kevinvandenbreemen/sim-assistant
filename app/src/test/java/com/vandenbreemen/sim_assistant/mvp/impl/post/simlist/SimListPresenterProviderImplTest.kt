package com.vandenbreemen.sim_assistant.mvp.impl.post.simlist

import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.MainScreenPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.mainscreen.UserSettingsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GooglePostCacheInteractor
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowLog

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

    @Before
    fun setup(){

        val app = RuntimeEnvironment.application as SimAssistantApp

        ShadowLog.stream = System.out
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }
        simListPresenterProvider = SimListPresenterProviderImpl(
                app,
                UserSettingsInteractorImpl(app),
                GoogleGroupsInteractorImpl(app),
                GooglePostCacheInteractor(app)
        )
    }

    @Test
    fun shouldGeneratePresenterForGoogleGroup(){

        val mainScreenPresenter = MainScreenPresenterImpl(
                MainScreenModelImpl(UserSettingsInteractorImpl(RuntimeEnvironment.application as SimAssistantApp),
                        GoogleGroupsInteractorImpl(RuntimeEnvironment.application as SimAssistantApp)
                ),
                mock(MainScreenView::class.java)
        )

        mainScreenPresenter.start().blockingAwait()

        mainScreenPresenter.setGoogleGroupName("sb118-apollo")

        val simListPresenter = simListPresenterProvider.getSimListPresenter().blockingGet()

    }

}