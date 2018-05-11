package com.vandenbreemen.sim_assistant.di

import android.content.Context
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import dagger.Module
import dagger.Provides

@Module
class AppModule{

    @Provides
    fun provideContext(app:SimAssistantApp):Context{
        return app.applicationContext
    }

}