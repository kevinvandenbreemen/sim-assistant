package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
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

    @Before
    fun setup(){
        this.repository = TagRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
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

}