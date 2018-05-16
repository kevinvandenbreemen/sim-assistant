package com.vandenbreemen.sim_assistant.mvp.impl.mainscreen

import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenModel
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread

class MainScreenPresenterImpl(val mainScreenModelImpl: MainScreenModel, val view: MainScreenView) :MainScreenPresenter {
    override fun selectSimSource(simSource: SimSource) {
        when (simSource) {
            SimSource.GOOGLE_GROUP -> view.promptForGoogleGroupDetails()
        }
    }

    override fun start():Completable{

        return mainScreenModelImpl.checkShouldPromptUserForSimSource().observeOn(mainThread()).flatMapCompletable { shouldCheck ->
            CompletableSource {
                if(shouldCheck){
                    view.showSimSourceSelector(mainScreenModelImpl.getPossibleSimSources())
                }
                it.onComplete()
            }
        }
    }

}