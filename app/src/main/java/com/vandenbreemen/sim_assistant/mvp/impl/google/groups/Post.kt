package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
class Post {

    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null

}