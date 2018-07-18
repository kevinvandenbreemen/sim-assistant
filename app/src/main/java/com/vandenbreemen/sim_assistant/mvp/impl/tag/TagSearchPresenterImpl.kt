package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchRouter
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchView

class TagSearchPresenterImpl(val tagInteractor: TagInteractorImpl, val tagSimSearchView: TagSimSearchView, val tagSimSearchRouter: TagSimSearchRouter) :
        TagSimSearchPresenter {
    override fun searchTag(tagNameCriteria: String) {
        tagInteractor.searchTags(tagNameCriteria).subscribe { tags ->
            tagSimSearchView.displayTags(tags)
        }
    }


}