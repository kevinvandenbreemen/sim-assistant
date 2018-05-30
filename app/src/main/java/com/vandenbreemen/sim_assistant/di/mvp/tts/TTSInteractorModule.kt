package com.vandenbreemen.sim_assistant.di.mvp.tts

import android.app.Activity
import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractorImpl
import dagger.Module
import dagger.Provides

/**
 *
 * @author kevin
 */
@Module
class TTSInteractorModule {

    @Provides
    fun providesTTSInteractor(activity:ViewSimActivity): TTSInteractor{
        return TTSInteractorImpl(activity.application.applicationContext)
    }

}