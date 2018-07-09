package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.api.sim.Tag_
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository

/**
 *
 * @author kevin
 */
class TagRepositoryImpl(val app: SimAssistantApp):TagRepository {
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

        val tag = Tag(0, name)
        app.boxStore.boxFor(Tag::class.java).put(tag)
    }

    fun findTag(name:String):Tag?{
        return app.boxStore.boxFor(Tag::class.java).query().equal(Tag_.name, name)
                .build().findFirst()?.let {
                    return it
                }?:run{ return null }
    }
}