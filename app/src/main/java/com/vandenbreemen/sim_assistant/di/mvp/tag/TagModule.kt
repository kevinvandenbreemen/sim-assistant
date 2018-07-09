package com.vandenbreemen.sim_assistant.di.mvp.tag

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.fragments.SimTagManagerFragment
import com.vandenbreemen.sim_assistant.mvp.impl.tag.SimTagManagerPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.impl.tag.TagInteractorImpl
import com.vandenbreemen.sim_assistant.mvp.impl.tag.TagRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.TagInteractor
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
import dagger.Module
import dagger.Provides

@Module
class TagModule {

    @Provides
    fun providesTagRepository(fragment: SimTagManagerFragment): TagRepository {
        return TagRepositoryImpl(fragment.activity!!.application as SimAssistantApp)
    }

    @Provides
    fun providesTagInteractor(tagRepository: TagRepository): TagInteractor {
        return TagInteractorImpl(tagRepository)
    }

    @Provides
    fun providesSimTagManagerPresenter(tagInteractor: TagInteractor, fragment: SimTagManagerFragment): SimTagManagerPresenter {
        return SimTagManagerPresenterImpl(tagInteractor, fragment)
    }

}