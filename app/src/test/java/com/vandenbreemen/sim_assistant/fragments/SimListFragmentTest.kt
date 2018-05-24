package com.vandenbreemen.sim_assistant.fragments

import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.impl.post.simlist.SimListModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.post.simlist.SimListPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.util.FragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
class SimListFragmentTest{

    @get:Rule
    val mockingRule = MockitoJUnit.rule()

    @Mock
    lateinit var postRepository: PostRepository

    lateinit var presenter:SimListPresenter

    @Before
    fun setup(){
        presenter = SimListPresenterImpl(SimListModelImpl(postRepository))
    }

    @Test
    fun shouldListSims(){
        //  Arrange
        val sim = Sim(
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts()).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()

        //  Assert
        assertEquals("Sim list", 1, listView.childCount)
    }

    @Test
    fun shouldShowSimTitle(){
        //  Arrange
        val sim = Sim(
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts()).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()
        val simItem = listView.getChildAt(0) as ViewGroup

        //  Assert
        assertEquals("Sim Title",
                sim.title,
                simItem.findViewById<TextView>(R.id.simTitle).text)
    }

    @Test
    fun shouldShowSimAuthor(){
        //  Arrange
        val sim = Sim(
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts()).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()
        val simItem = listView.getChildAt(0) as ViewGroup

        //  Assert
        assertEquals("Sim Author",
                sim.author,
                simItem.findViewById<TextView>(R.id.simAuthor).text)
    }

}