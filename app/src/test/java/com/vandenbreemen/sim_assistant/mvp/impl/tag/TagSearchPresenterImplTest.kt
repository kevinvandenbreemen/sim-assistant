package com.vandenbreemen.sim_assistant.mvp.impl.tag

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.TagRepository
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchRouter
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchView
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TagSearchPresenterImplTest {

    @Mock
    lateinit var tagRepository: TagRepository

    lateinit var tagSearchPresenter: TagSimSearchPresenter

    @Mock
    lateinit var tagSimSearchView: TagSimSearchView

    @Mock
    lateinit var tagSimSearchRouter: TagSimSearchRouter

    val tag1 = Tag(1, "tag 1")
    val tag2 = Tag(2, "tag 2")

    val sim1 = Sim(1, "Kevin", "Test", 0, "Content")

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { trampoline() }
        tagSearchPresenter = TagSearchPresenterImpl(TagInteractorImpl(tagRepository),
                SimTagInteractorImpl(tagRepository),
                tagSimSearchView, tagSimSearchRouter)
    }

    @Test
    fun shouldSearchAndDisplayTags() {
        //  Arrange
        `when`(tagRepository.searchTag("tag")).thenReturn(listOf(tag1, tag2))
        `when`(tagRepository.hasSims(tag1)).thenReturn(true)
        `when`(tagRepository.hasSims(tag2)).thenReturn(true)

        //  Act
        tagSearchPresenter.searchTag("tag")

        //  Assert
        verify(tagSimSearchView).displayTags(listOf(tag1, tag2))
    }

    @Test
    fun shouldExcludeTagsThatAreUnUsedInSearchResults() {
        //  Arrange
        `when`(tagRepository.searchTag("tag")).thenReturn(listOf(tag1, tag2))
        `when`(tagRepository.hasSims(tag1)).thenReturn(true)
        `when`(tagRepository.hasSims(tag2)).thenReturn(false)

        //  Act
        tagSearchPresenter.searchTag("tag")

        //  Assert
        verify(tagSimSearchView).displayTags(listOf(tag1))
    }

    @Test
    fun shouldReturnBlankListWithoutErrorIfNoResult() {
        //  Arrange
        `when`(tagRepository.searchTag("tag")).thenReturn(emptyList())

        //  Act
        tagSearchPresenter.searchTag("tag")

        //  Assert
        verify(tagSimSearchView).displayTags(emptyList())
    }

    @Test
    fun shouldGoToSimListOnceTagSelected() {
        //  Arrange
        `when`(tagRepository.getSims(tag1)).thenReturn(listOf(sim1))

        //  Act
        tagSearchPresenter.selectTag(tag1)

        //  Assert
        tagSimSearchRouter.gotoSimList(listOf(sim1))
    }

}