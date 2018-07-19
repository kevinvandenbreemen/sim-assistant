package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.SimTag
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class TagRepositoryImplTest{

    lateinit var repository:TagRepository
    lateinit var app: SimAssistantApp

    @Before
    fun setup(){
        this.repository = TagRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
        app = RuntimeEnvironment.application as SimAssistantApp
    }

    @Test
    fun shouldCreateNewTag(){

        //  Act
        repository.addTag("Test")

        //  Assert
        val tags = repository.getTags()
        assertEquals("Single Tag", 1, tags.size)
        assertEquals("Stored tag", 1L, tags[0].id)
        assertEquals("Stored tag", "Test", tags[0].name)
    }

    @Test
    fun shouldNotAllowTagWithBlankName(){
        //  Act
        try{
            repository.addTag("")
            fail("Should not have added tag")
        }
        catch(error:ApplicationError){

        }

        assertEquals("No tags", 0, repository.getTags().size)


    }

    @Test
    fun shouldNotAllowAddingTagWithSameName(){
        //  Arrange
        repository.addTag("test")

        //  Act
        try{
            repository.addTag("test")
            fail("Should not have allowed this")
        }
        catch(error:ApplicationError){

        }

        //  Assert
        assertEquals("Single Tag", 1, repository.getTags().size)
    }

    @Test
    fun shouldNotAllowAddingTagWithSameNameDifferentCase() {
        //  Arrange
        repository.addTag("test")

        //  Act
        try {
            repository.addTag("TEST")
            fail("Should not have allowed this")
        } catch (error: ApplicationError) {

        }

        //  Assert
        assertEquals("Single Tag", 1, repository.getTags().size)
    }

    @Test
    fun shouldRecognizeWhenSimHasTag() {
        //  Arrange
        val sim = Sim(0, "Kevin", "Test", 0, "Content")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        val simTag = SimTag(0, sim.id, tag.id)
        app.boxStore.boxFor(SimTag::class.java).put(simTag)

        //  Act/assert
        assertTrue("Has Tag", repository.hasTag(sim, tag))
    }

    @Test
    fun shouldRemoveTag() {
        //  Arrange
        val sim = Sim(0, "Kevin", "Test", 0, "Content")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        val simTag = SimTag(0, sim.id, tag.id)
        app.boxStore.boxFor(SimTag::class.java).put(simTag)

        //  Act
        repository.removeTag(sim, tag)

        //  Assert
        assertFalse("Has Tag", repository.hasTag(sim, tag))
    }

    @Test
    fun shouldRemoveOnlySelectedTag() {
        //  Arrange
        val sim = Sim(0, "Kevin", "Test", 0, "Content")
        app.boxStore.boxFor(Sim::class.java).put(sim)

        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)
        val anotherTag = Tag(0, "Another")
        app.boxStore.boxFor(Tag::class.java).put(anotherTag)

        val simTag = SimTag(0, sim.id, tag.id)
        app.boxStore.boxFor(SimTag::class.java).put(simTag)
        val newSimTag = SimTag(0, sim.id, anotherTag.id)
        app.boxStore.boxFor(SimTag::class.java).put(newSimTag)

        //  Act
        repository.removeTag(sim, anotherTag)

        //  Assert
        assertFalse("Has Tag", repository.hasTag(sim, anotherTag))
        assertTrue("Has Tag", repository.hasTag(sim, tag))
    }

    @Test
    fun shouldSearchTag() {
        //  Arrange
        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        val tags = repository.searchTag("Test")

        //  Assert
        assertEquals("Single Tag", 1, tags.size)
        assertEquals("First Tag", tag, tags[0])
    }

    @Test
    fun shouldSearchTagIgnoringCase() {
        //  Arrange
        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        val tags = repository.searchTag("test")

        //  Assert
        assertEquals("Single Tag", 1, tags.size)
        assertEquals("First Tag", tag, tags[0])
    }

    @Test
    fun shouldSearchSubstrings() {
        //  Arrange
        val tag = Tag(0, "Test")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        val tags = repository.searchTag("te")

        //  Assert
        assertEquals("Single Tag", 1, tags.size)
        assertEquals("First Tag", tag, tags[0])
    }

    @Test
    fun shouldSearchForTextWithinTagName() {
        //  Arrange
        val tag = Tag(0, "Test Tag")
        app.boxStore.boxFor(Tag::class.java).put(tag)

        //  Act
        val tags = repository.searchTag("tag")

        //  Assert
        assertEquals("Single Tag", 1, tags.size)
        assertEquals("First Tag", tag, tags[0])
    }

}