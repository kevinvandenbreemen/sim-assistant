package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.*

class TagSearchPresenterImpl(val tagInteractor: TagInteractor, val simTagInteractor: SimTagInteractor, val tagSimSearchView: TagSimSearchView, val tagSimSearchRouter: TagSimSearchRouter) :
        TagSimSearchPresenter {
    override fun selectTag(tag: Tag) {
        simTagInteractor.getSims(tag).subscribe { sims -> tagSimSearchRouter.gotoSimList(sims) }
    }

    override fun searchTag(tagNameCriteria: String) {
        tagInteractor.searchTags(tagNameCriteria).subscribe { tags ->
            tagSimSearchView.displayTags(tags)
        }
    }


}