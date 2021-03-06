package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagInteractor
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerView
import com.vandenbreemen.sim_assistant.mvp.tag.TagInteractor
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread

/**
 *
 * @author kevin
 */
//
class SimTagManagerPresenterImpl(val tagInteractor: TagInteractor, val simTagInteractor: SimTagInteractor, val view: SimTagManagerView) : SimTagManagerPresenter {
    override fun start(sim: Sim) {
        simTagInteractor.getTags(sim).subscribe { tags ->
            view.listTags(tags)
        }
    }

    override fun addTag(name: String) {
        tagInteractor.addTag(name).observeOn(mainThread()).subscribe({
            tagInteractor.getTags().observeOn(mainThread()).subscribe { tags -> view.listTags(tags) }
        },{error->
            if(error is ApplicationError){
                view.showError(error)
            }
        })
    }

    override fun toggleSimTag(sim: Sim, tag: Tag) {
        simTagInteractor.toggleTag(sim, tag).subscribe({
            simTagInteractor.getTags(sim).observeOn(mainThread())
                    .subscribe({ tags -> view.listTags(tags) })
        })
    }
}