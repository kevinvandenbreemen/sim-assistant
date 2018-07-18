package com.vandenbreemen.sim_assistant.mvp.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag

interface TagSimSearchPresenter {
    fun searchTag(tagNameCriteria: String)
    fun selectTag(tag: Tag)

}

interface TagSimSearchView {
    fun displayTags(tags: List<Tag>)

}

interface TagSimSearchRouter {
    fun gotoSimList(sims: List<Sim>)

}