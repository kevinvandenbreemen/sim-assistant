package com.vandenbreemen.sim_assistant.mvp.impl.tag

import android.util.Log
import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.*
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository

/**
 *
 * @author kevin
 */
class TagRepositoryImpl(val app: SimAssistantApp):TagRepository {
    override fun searchTag(tagNameCriteria: String): List<Tag> {
        return app.boxStore.boxFor(Tag::class.java).query()
                .contains(Tag_.name, tagNameCriteria)
                .build().find()
    }

    override fun getSims(tag: Tag): List<Sim> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val TAG = "TagRepository"
    }

    override fun hasTag(sim: Sim, tag: Tag): Boolean {
        return app.boxStore.boxFor(SimTag::class.java).query()
                .equal(SimTag_.simId, sim.id).equal(SimTag_.tagId, tag.id)
                .build().count() == 1L
    }

    override fun removeTag(sim: Sim, tag: Tag) {
        app.boxStore.boxFor(SimTag::class.java).query()
                .equal(SimTag_.simId, sim.id).equal(SimTag_.tagId, tag.id)
                .build().remove()
    }


    override fun tagSim(sim: Sim, tag: Tag) {
        val newTag = SimTag(0, sim.id, tag.id)
        app.boxStore.boxFor(SimTag::class.java).put(newTag)
    }

    override fun getTags(): List<Tag> {
        return app.boxStore.boxFor(Tag::class.java).all
    }

    override fun addTag(name: String) {
        if(name.isBlank()){
            throw ApplicationError("Tag must have a name")
        }

        findTag(name)?.let {
            throw ApplicationError("Tag '$name' already exists")
        }

        val tag = Tag(0, name, false)
        app.boxStore.boxFor(Tag::class.java).put(tag)
    }

    private fun load(id: Long): Tag {
        return app.boxStore.boxFor(Tag::class.java).query().equal(Tag_.id, id).build().findFirst()!!
    }

    fun findTag(name:String):Tag?{
        return app.boxStore.boxFor(Tag::class.java).query().equal(Tag_.name, name)
                .build().findFirst()?.let {
                    return it
                }?:run{ return null }
    }

    override fun getTags(sim: Sim): List<Tag> {
        return app.boxStore.boxFor(SimTag::class.java).query().equal(SimTag_.simId, sim.id).build().find()
                .map { simTag ->
                    Log.d(TAG, "Load simTag:  $simTag")
                    return@map load(simTag.tagId)
                }
    }
}