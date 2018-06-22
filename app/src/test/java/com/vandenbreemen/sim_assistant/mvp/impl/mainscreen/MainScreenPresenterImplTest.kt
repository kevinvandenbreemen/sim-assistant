package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.usersettings.UserSettingsRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenModel
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * <h2>Intro</h2>
 *
 *
 * <h2>Other Details</h2>
 *
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class MainScreenPresenterImplTest{

    @get:Rule
    val mockingRule = MockitoJUnit.rule()

    @Mock
    lateinit var view:MainScreenView

    @Mock
    lateinit var mockedModel : MainScreenModel

    lateinit var mainScreenPresenter: MainScreenPresenter

    @Before
    fun setup(){

        RxJavaPlugins.setIoSchedulerHandler { mainThread() }

        mainScreenPresenter = MainScreenPresenterImpl(
                MainScreenModelImpl(UserSettingsInteractorImpl(UserSettingsRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)),
                        GoogleGroupsInteractorImpl(GoogleGroupRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp))
                        ),
                view
        )
    }

    @Test
    fun shouldShowSimSourceSelectorOnFirstRun(){
        mainScreenPresenter.start().blockingAwait()
        verify(view).showSimSourceSelector(SimSource.values().asList())

    }

    @Test
    fun shouldCallCheckFlag(){
        //  Arrange
        val presenter = MainScreenPresenterImpl(mockedModel, view)
        `when`(mockedModel.checkShouldPromptUserForSimSource()).thenReturn(Single.just(true))

        presenter.start().blockingAwait()

        verify(mockedModel).checkShouldPromptUserForSimSource()
    }

    @Test
    fun shouldPromptForGoogleGroupDetails() {
        //  Arrange
        mainScreenPresenter.start().blockingAwait()

        //  Act
        mainScreenPresenter.selectSimSource(SimSource.GOOGLE_GROUP)

        //  Assert
        verify(view).promptForGoogleGroupDetails()
    }

    @Test
    fun shouldStartSimListWhenSimSourceAlreadyConfigured(){

        //  Arrange
        val presenter = MainScreenPresenterImpl(mockedModel, view)
        `when`(mockedModel.checkShouldPromptUserForSimSource()).thenReturn(Single.just(false))

        //  Act
        presenter.start().blockingAwait()

        //  Assert
        verify(view).showSimList()

    }

    @Test
    fun shouldConfigureSourceAsGoogleGroupOnceGoogleGroupNameSpecified() {

        //  Arrange
        val presenter = MainScreenPresenterImpl(mockedModel, view)
        `when`(mockedModel.addGoogleGroup("sb118-apollo")).thenReturn(Completable.complete())
        `when`(mockedModel.setSimSource(SimSource.GOOGLE_GROUP)).thenReturn(Completable.complete())

        //  Act
        presenter.setGoogleGroupName("sb118-apollo")

        //  Assert
        verify(mockedModel).addGoogleGroup("sb118-apollo")
        verify(mockedModel).setSimSource(SimSource.GOOGLE_GROUP)

    }

    @Test
    fun shouldStartSimListWorkflowAfterSetGoogleGroupName(){

        //  Arrange
        mainScreenPresenter.start().blockingAwait()

        //  Act
        mainScreenPresenter.setGoogleGroupName("sb118-apollo")

        //  Assert
        verify(view).showSimList()
    }

    @Test
    fun shouldPreventSpecifyingBlankGoogleGroupName(){

        //  Arrange
        mainScreenPresenter.start().blockingAwait()

        //  Act
        mainScreenPresenter.setGoogleGroupName("")

        //  Assert
        verify(view).showError("Please specify group name")
        verify(view, never()).showSimList()
    }

    @Test
    fun shouldRaiseErrorOnNonExistentGroup(){
        //  Arrange
        mainScreenPresenter.start().blockingAwait()

        //  Act
        mainScreenPresenter.setGoogleGroupName("no-such-group-testonly")

        //  Assert
        verify(view).showError("no-such-group-testonly:  No such Google Group")
        verify(view, never()).showSimList()
    }

}