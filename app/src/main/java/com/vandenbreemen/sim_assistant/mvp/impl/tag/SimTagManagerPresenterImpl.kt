package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagInteractor
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerView
import com.vandenbreemen.sim_assistant.mvp.tag.TagInteractor

/**
 *
 * @author kevin
 */
//
class SimTagManagerPresenterImpl(val tagInteractor: TagInteractor, val simTagInteractor: SimTagInteractor, val view: SimTagManagerView) : SimTagManagerPresenter {
    override fun start() {
        tagInteractor.getTags().subscribe { tags ->
            view.listTags(tags)
        }
    }

    override fun addTag(name: String) {
        tagInteractor.addTag(name).subscribe({
            tagInteractor.getTags().subscribe { tags->view.listTags(tags) }
        },{error->
            if(error is ApplicationError){
                view.showError(error)
            }
        })
    }

    override fun addTag(sim: Sim, tag: Tag) {

    }
}