package com.vandenbreemen.sim_assistant.di

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    BuildersModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(app:SimAssistantApp):Builder
        fun build():AppComponent
    }

    fun inject(app:SimAssistantApp)

}