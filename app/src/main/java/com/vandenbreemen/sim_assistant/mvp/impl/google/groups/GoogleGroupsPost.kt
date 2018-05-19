package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class GoogleGroupsPost(

        @set:Element(name = "title")
    @get:Element(name = "title")
        var title: String? = null,

        @set:Element(name = "link")
        @get:Element(name = "link")
        var link: String? = null

) {}